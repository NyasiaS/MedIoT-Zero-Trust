// Control Plane/src/main/java/com/nyasia/controlplane/auth/AuthController.java
package com.nyasia.controlplane.auth;

import com.nyasia.controlplane.model.DeviceEntity;
import com.nyasia.controlplane.repo.DeviceRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.jwt.*;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtEncoder jwtEncoder;
    private final DeviceRepo devices;
    private final DeviceKeyService keyService;
    private final String issuer;
    private final long ttlSeconds;

    public AuthController(
            JwtEncoder jwtEncoder,
            DeviceRepo devices,
            DeviceKeyService keyService,
            @Value("${security.jwt.issuer}") String issuer,
            @Value("${security.jwt.ttl-seconds}") long ttlSeconds
    ) {
        this.jwtEncoder = jwtEncoder;
        this.devices = devices;
        this.keyService = keyService;
        this.issuer = issuer;
        this.ttlSeconds = ttlSeconds;
    }

    @PostMapping("/token")
    public TokenResponse token(@RequestBody TokenRequest req) {
        UUID deviceId = UUID.fromString(req.deviceId());
        DeviceEntity d = devices.findById(deviceId).orElseThrow(() -> new Unauthorized("Unknown device"));

        if (!d.isEnabled()) throw new Unauthorized("Device disabled");
        if (d.getApiKeyHash() == null) throw new Unauthorized("Device not registered");

        String presentedHash = keyService.hmacSha256(req.apiKey());
        if (!presentedHash.equals(d.getApiKeyHash())) {
            d.setFailedAuthCount(d.getFailedAuthCount() + 1);
            d.setLastFailedAuthAt(Instant.now());
            devices.save(d);
            throw new Unauthorized("Invalid apiKey");
        }

        // success: reset counters + issue token
        d.setFailedAuthCount(0);
        d.setLastFailedAuthAt(null);
        devices.save(d);

        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer(issuer)
                .issuedAt(now)
                .expiresAt(now.plusSeconds(ttlSeconds))
                .subject(d.getUuid().toString())
                .claim("role", d.getRole())
                .build();

        String jwt = jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
        return new TokenResponse(jwt, ttlSeconds);
    }

    public record TokenRequest(String deviceId, String apiKey) {}
    public record TokenResponse(String accessToken, long expiresInSeconds) {}

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public static class Unauthorized extends RuntimeException {
        public Unauthorized(String msg) { super(msg); }
    }
}