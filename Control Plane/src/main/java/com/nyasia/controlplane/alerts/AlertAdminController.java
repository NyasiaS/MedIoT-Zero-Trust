package com.nyasia.controlplane.alerts;

import com.nyasia.controlplane.model.AlertEntity;
import com.nyasia.controlplane.repo.AlertRepo;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/alerts")
public class AlertAdminController {

    private final AlertRepo alerts;

    public AlertAdminController(AlertRepo alerts) {
        this.alerts = alerts;
    }

    @PatchMapping("/{id}/ack")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void ack(@PathVariable Long id) {
        AlertEntity a = alerts.findById(id).orElseThrow();
        a.setStatus(AlertEntity.Status.ACKED);
        alerts.save(a);
    }

    @PatchMapping("/{id}/resolve")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void resolve(@PathVariable Long id) {
        AlertEntity a = alerts.findById(id).orElseThrow();
        a.setStatus(AlertEntity.Status.RESOLVED);
        alerts.save(a);
    }
}