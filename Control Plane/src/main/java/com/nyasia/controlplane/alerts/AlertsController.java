// Control Plane/src/main/java/com/nyasia/controlplane/alerts/AlertsController.java
package com.nyasia.controlplane.alerts;

import com.nyasia.controlplane.model.AlertEntity;
import com.nyasia.controlplane.repo.AlertRepo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/alerts")
public class AlertsController {

    private final AlertRepo alerts;

    public AlertsController(AlertRepo alerts) {
        this.alerts = alerts;
    }

    @GetMapping
    public List<AlertEntity> list() {
        return alerts.findAll();
    }
}

