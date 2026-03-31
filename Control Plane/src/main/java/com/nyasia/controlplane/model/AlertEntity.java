// Control Plane/src/main/java/com/nyasia/controlplane/model/AlertEntity.java
package com.nyasia.controlplane.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "alerts")
public class AlertEntity {

    public enum Status { OPEN, ACKED, RESOLVED }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "device_id", nullable = false, length = 128)
    private String deviceId;

    @Column(name = "type", nullable = false, length = 64)
    private String type;

    @Column(name = "severity", nullable = false, length = 32)
    private String severity;

    @Column(name = "message", length = 1024)
    private String message;

    @Column(name = "ts", nullable = false, updatable = false)
    private Instant ts;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 16)
    private Status status = Status.OPEN;

    @Column(name = "first_seen")
    private Instant firstSeen;

    @Column(name = "last_seen")
    private Instant lastSeen;

    @Column(name = "count", nullable = false)
    private int count = 1;

    public AlertEntity() {}

    @PrePersist
    private void prePersist() {
        if (ts == null) ts = Instant.now();
        if (firstSeen == null) firstSeen = ts;
        if (lastSeen == null) lastSeen = ts;
        if (status == null) status = Status.OPEN;
        if (count <= 0) count = 1;
    }

    public Long getId() { return id; }
    public String getDeviceId() { return deviceId; }
    public String getType() { return type; }
    public String getSeverity() { return severity; }
    public String getMessage() { return message; }
    public Instant getTs() { return ts; }
    public Status getStatus() { return status; }
    public Instant getFirstSeen() { return firstSeen; }
    public Instant getLastSeen() { return lastSeen; }
    public int getCount() { return count; }

    public void setId(Long id) { this.id = id; }
    public void setDeviceId(String deviceId) { this.deviceId = deviceId; }
    public void setType(String type) { this.type = type; }
    public void setSeverity(String severity) { this.severity = severity; }
    public void setMessage(String message) { this.message = message; }
    public void setTs(Instant ts) { this.ts = ts; }
    public void setStatus(Status status) { this.status = status; }
    public void setFirstSeen(Instant firstSeen) { this.firstSeen = firstSeen; }
    public void setLastSeen(Instant lastSeen) { this.lastSeen = lastSeen; }
    public void setCount(int count) { this.count = count; }
}