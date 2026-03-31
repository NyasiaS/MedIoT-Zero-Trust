// Control Plane/src/main/java/com/nyasia/controlplane/auth/DeviceKeyService.java
package com.nyasia.controlplane.auth;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.Base64;

@Service
public class DeviceKeyService {

    private final byte[] secret;
    private final SecureRandom random = new SecureRandom();

    public DeviceKeyService(@Value("${security.device-key.secret}") String secret) {
        this.secret = secret.getBytes(StandardCharsets.UTF_8);
    }

    /** Returns a one-time plaintext key suitable for showing to the device owner once. */
    public String generateApiKey() {
        byte[] bytes = new byte[32]; // 256-bit
        random.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }

    /** Returns a stable HMAC-SHA256 hash for storage/verification. */
    public String hmacSha256(String apiKey) {
        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(secret, "HmacSHA256"));
            byte[] out = mac.doFinal(apiKey.getBytes(StandardCharsets.UTF_8));
            return Base64.getUrlEncoder().withoutPadding().encodeToString(out);
        } catch (Exception e) {
            throw new IllegalStateException("Unable to compute HMAC-SHA256", e);
        }
    }

    public Instant now() {
        return Instant.now();
    }
}