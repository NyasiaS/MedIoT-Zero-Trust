// Control Plane/src/main/java/com/nyasia/controlplane/events/AlertService.java
package com.nyasia.controlplane.events;

import com.nyasia.controlplane.model.AlertEntity;
import com.nyasia.controlplane.model.EventEntity;
import com.nyasia.controlplane.repo.EventRepo;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
public class AlertService {
    private final EventRepo events;

    public AlertService(EventRepo events) {
        this.events = events;
    }

    public AlertEntity evaluate(String deviceId) {
        Instant after = Instant.now().minusSeconds(60);
        List<EventEntity> lastMin = events.findByDeviceIdAndTsAfter(deviceId, after);

        long blocked = lastMin.stream()
                .filter(e -> "BLOCKED".equalsIgnoreCase(e.getOutcome()))
                .count();

        if (blocked >= 5) {
            AlertEntity a = new AlertEntity();
            a.setDeviceId(deviceId);
            a.setType("PROBING");
            a.setSeverity(blocked >= 10 ? "HIGH" : "MED");
            return a;
        }
        return null;
    }
}
