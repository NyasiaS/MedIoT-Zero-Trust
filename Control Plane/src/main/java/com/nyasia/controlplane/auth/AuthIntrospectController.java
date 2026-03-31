// Control Plane/src/main/java/com/nyasia/controlplane/auth/AuthIntrospectController.java
package com.nyasia.controlplane.auth;

import com.nyasia.controlplane.model.DeviceEntity;
import com.nyasia.controlplane.repo.DeviceRepo;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/auth")
public class AuthIntrospectController {

    private final DeviceRepo devices;

    public AuthIntrospectController(DeviceRepo devices) {
        this.devices = devices;
    }

    @GetMapping("/introspect/{deviceId}")
    public IntrospectResponse introspect(@PathVariable UUID deviceId) {
        DeviceEntity d = devices.findById(deviceId).orElseThrow();
        return new IntrospectResponse(d.isEnabled(), d.getRole());
    }

    public record IntrospectResponse(boolean enabled, String role) {}
}