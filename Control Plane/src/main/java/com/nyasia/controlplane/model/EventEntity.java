package com.nyasia.controlplane.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "events")
public class EventEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Instant ts;

    @Column(nullable = false)
    private String deviceId;

    private String role;
    private String method;
    private String path;
    private String outcome;
    private String reason;

    public EventEntity() {}

    public EventEntity(Instant ts, String deviceId, String role, String method, String path, String outcome, String reason) {
        this.ts = ts;
        this.deviceId = deviceId;
        this.role = role;
        this.method = method;
        this.path = path;
        this.outcome = outcome;
        this.reason = reason;
    }

    public Long getId() { return id; }
    public Instant getTs() { return ts; }
    public String getDeviceId() { return deviceId; }
    public String getRole() { return role; }
    public String getMethod() { return method; }
    public String getPath() { return path; }
    public String getOutcome() { return outcome; }
    public String getReason() { return reason; }

    public void setId(Long id) { this.id = id; }
    public void setTs(Instant ts) { this.ts = ts; }
    public void setDeviceId(String deviceId) { this.deviceId = deviceId; }
    public void setRole(String role) { this.role = role; }
    public void setMethod(String method) { this.method = method; }
    public void setPath(String path) { this.path = path; }
    public void setOutcome(String outcome) { this.outcome = outcome; }
    public void setReason(String reason) { this.reason = reason; }
}
