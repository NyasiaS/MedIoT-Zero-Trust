// Control Plane/src/main/java/com/nyasia/controlplane/devices/DeviceRegistrationController.java
package com.nyasia.controlplane.devices;

import com.nyasia.controlplane.auth.DeviceKeyService;
import com.nyasia.controlplane.model.DeviceEntity;
import com.nyasia.controlplane.repo.DeviceRepo;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.UUID;

@RestController
@RequestMapping("/devices")
public class DeviceRegistrationController {

    private final DeviceRepo devices;
    private final DeviceKeyService keyService;

    public DeviceRegistrationController(DeviceRepo devices, DeviceKeyService keyService) {
        this.devices = devices;
        this.keyService = keyService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public RegisterResponse register(@RequestBody RegisterRequest req) {
        String apiKey = keyService.generateApiKey();
        String apiKeyHash = keyService.hmacSha256(apiKey);
        Instant now = keyService.now();

        DeviceEntity d = new DeviceEntity();
        d.setDeviceName(req.deviceName());
        d.setRole(req.role());
        d.setApiKeyHash(apiKeyHash);
        d.setApiKeyCreatedAt(now);
        d.setApiKeyLastRotatedAt(now);

        DeviceEntity saved = devices.save(d);
        return new RegisterResponse(saved.getUuid(), saved.getRole(), apiKey);
    }

    public record RegisterRequest(String deviceName, String role) {}
    public record RegisterResponse(UUID deviceId, String role, String apiKey) {}
}