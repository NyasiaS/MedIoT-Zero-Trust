// Control Plane/src/main/java/com/nyasia/controlplane/events/EventController.java
package com.nyasia.controlplane.events;

import com.nyasia.controlplane.alerts.AlertDeduper;
import com.nyasia.controlplane.model.AlertEntity;
import com.nyasia.controlplane.model.DeviceEntity;
import com.nyasia.controlplane.model.EventEntity;
import com.nyasia.controlplane.repo.DeviceRepo;
import com.nyasia.controlplane.repo.EventRepo;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/events")
public class EventController {

    private final EventRepo events;
    private final DeviceRepo devices;
    private final AlertService alertService;
    private final AlertDeduper alertDeduper;

    public EventController(
            EventRepo events,
            DeviceRepo devices,
            AlertService alertService,
            AlertDeduper alertDeduper
    ) {
        this.events = events;
        this.devices = devices;
        this.alertService = alertService;
        this.alertDeduper = alertDeduper;
    }

    @GetMapping
    public List<EventEntity> list() {
        return events.findAll();
    }

    @PostMapping("/ingest")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void ingest(@Valid @RequestBody IngestEventRequest req) {
        Instant now = Instant.now();

        EventEntity e = new EventEntity();
        e.setDeviceId(req.deviceId());
        e.setOutcome(req.outcome());
        e.setTs(req.ts() != null ? req.ts() : now);

        events.save(e);

        devices.findByIdSafe(req.deviceId()).ifPresent(d -> touchLastSeen(d, now));

        AlertEntity a = alertService.evaluate(req.deviceId());
        if (a != null) {
            alertDeduper.upsert(a);
        }
    }

    private void touchLastSeen(DeviceEntity d, Instant now) {
        d.setLastSeen(now);
        devices.save(d);
    }

    /**
     * Minimal request schema.
     * If your Gateway forwards more fields, keep them here (role/action/payload),
     * but only persist them if EventEntity supports them.
     */
    public record IngestEventRequest(
            @NotBlank @Size(max = 128) String deviceId,
            @NotBlank @Size(max = 32) String outcome,
            Instant ts,
            @Size(max = 64) String role,
            @Size(max = 64) String action,
            @Size(max = 2048) String payload
    ) {}
}