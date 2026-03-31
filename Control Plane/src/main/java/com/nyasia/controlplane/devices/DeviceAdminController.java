// src/main/java/com/nyasia/controlplane/devices/DeviceAdminController.java
package com.nyasia.controlplane.devices;

import com.nyasia.controlplane.model.DeviceEntity;
import com.nyasia.controlplane.repo.DeviceRepo;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/devices")
public class DeviceAdminController {

    private final DeviceRepo devices;

    public DeviceAdminController(DeviceRepo devices) {
        this.devices = devices;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DeviceEntity create(@RequestBody CreateDeviceRequest req) {
        DeviceEntity d = new DeviceEntity();
        d.setDeviceName(req.deviceName());
        d.setRole(req.role());
        return devices.save(d);
    }

    @GetMapping
    public List<DeviceEntity> list() {
        return devices.findAll();
    }

    @PatchMapping("/{id}/disable")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void disable(@PathVariable UUID id) {
        DeviceEntity d = devices.findById(id).orElseThrow();
        d.setEnabled(false);
        devices.save(d);
    }

    public record CreateDeviceRequest(String deviceName, String role) {}
}
