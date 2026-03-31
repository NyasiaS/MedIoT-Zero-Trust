// Control Plane/src/main/java/com/nyasia/controlplane/alerts/AlertDeduper.java

package com.nyasia.controlplane.alerts;

import com.nyasia.controlplane.model.AlertEntity;
import com.nyasia.controlplane.repo.AlertRepo;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class AlertDeduper {

    private final AlertRepo alerts;
    private final long dedupeWindowSeconds;

    public AlertDeduper(AlertRepo alerts,
                        @Value("${alerts.dedupe.window-seconds:60}") long dedupeWindowSeconds) {
        this.alerts = alerts;
        this.dedupeWindowSeconds = dedupeWindowSeconds;
    }

    public AlertEntity upsert(AlertEntity incoming) {
        Instant now = Instant.now();

        return alerts.findTopByDeviceIdAndTypeAndStatusOrderByTsDesc(
                        incoming.getDeviceId(),
                        incoming.getType(),
                        AlertEntity.Status.OPEN
                )
                .filter(existing -> existing.getTs() != null
                        && existing.getTs().isAfter(now.minusSeconds(dedupeWindowSeconds)))
                .map(existing -> {
                    existing.setLastSeen(now);
                    existing.setCount(existing.getCount() + 1);

                    // severity escalation example
                    existing.setSeverity(escalate(existing.getCount(), existing.getSeverity()));
                    if (incoming.getMessage() != null && !incoming.getMessage().isBlank()) {
                        existing.setMessage(incoming.getMessage());
                    }
                    return alerts.save(existing);
                })
                .orElseGet(() -> {
                    incoming.setStatus(AlertEntity.Status.OPEN);
                    incoming.setTs(now);
                    incoming.setFirstSeen(now);
                    incoming.setLastSeen(now);
                    incoming.setCount(1);
                    return alerts.save(incoming);
                });
    }

    private String escalate(int count, String currentSeverity) {
        // Simple policy: >=10 HIGH else MED (or keep HIGH if already HIGH)
        if ("HIGH".equalsIgnoreCase(currentSeverity)) return "HIGH";
        return count >= 10 ? "HIGH" : "MED";
    }
}