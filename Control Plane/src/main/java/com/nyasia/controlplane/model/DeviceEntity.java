// Control Plane/src/main/java/com/nyasia/controlplane/model/DeviceEntity.java
package com.nyasia.controlplane.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "devices")
public class DeviceEntity {

    @Id
    @Column(name = "uuid", nullable = false, updatable = false, columnDefinition = "uuid")
    private UUID uuid;

    @Column(name = "device_name", nullable = false, length = 255)
    private String deviceName;

    @Column(name = "role", nullable = false, length = 64)
    private String role;

    @Column(name = "enabled", nullable = false)
    private boolean enabled = true;

    @Column(name = "last_seen")
    private Instant lastSeen;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @Column(name = "api_key_hash", length = 255)
    private String apiKeyHash;

    @Column(name = "api_key_created_at")
    private Instant apiKeyCreatedAt;

    @Column(name = "api_key_last_rotated_at")
    private Instant apiKeyLastRotatedAt;

    @Column(name = "failed_auth_count", nullable = false)
    private int failedAuthCount = 0;

    @Column(name = "last_failed_auth_at")
    private Instant lastFailedAuthAt;

    public DeviceEntity() {}

    @PrePersist
    private void prePersist() {
        if (uuid == null) uuid = UUID.randomUUID();
        if (createdAt == null) createdAt = Instant.now();
    }

    public UUID getUuid() { return uuid; }
    public String getDeviceName() { return deviceName; }
    public String getRole() { return role; }
    public boolean isEnabled() { return enabled; }
    public Instant getLastSeen() { return lastSeen; }
    public Instant getCreatedAt() { return createdAt; }
    public String getApiKeyHash() { return apiKeyHash; }
    public Instant getApiKeyCreatedAt() { return apiKeyCreatedAt; }
    public Instant getApiKeyLastRotatedAt() { return apiKeyLastRotatedAt; }
    public int getFailedAuthCount() { return failedAuthCount; }
    public Instant getLastFailedAuthAt() { return lastFailedAuthAt; }

    public void setUuid(UUID uuid) { this.uuid = uuid; }
    public void setDeviceName(String deviceName) { this.deviceName = deviceName; }
    public void setRole(String role) { this.role = role; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    public void setLastSeen(Instant lastSeen) { this.lastSeen = lastSeen; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public void setApiKeyHash(String apiKeyHash) { this.apiKeyHash = apiKeyHash; }
    public void setApiKeyCreatedAt(Instant apiKeyCreatedAt) { this.apiKeyCreatedAt = apiKeyCreatedAt; }
    public void setApiKeyLastRotatedAt(Instant apiKeyLastRotatedAt) { this.apiKeyLastRotatedAt = apiKeyLastRotatedAt; }
    public void setFailedAuthCount(int failedAuthCount) { this.failedAuthCount = failedAuthCount; }
    public void setLastFailedAuthAt(Instant lastFailedAuthAt) { this.lastFailedAuthAt = lastFailedAuthAt; }
}