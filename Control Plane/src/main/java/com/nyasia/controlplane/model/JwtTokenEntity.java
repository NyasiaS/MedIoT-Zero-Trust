package com.nyasia.controlplane.model;

import jakarta.persistence.*;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "jwt_tokens")
public class JwtTokenEntity {

    @Id
    @Column(name = "jti", nullable = false, length = 64)
    private String jti;

    @Column(name = "device_id", nullable = false, columnDefinition = "uuid")
    private UUID deviceId;

    @Column(name = "role", nullable = false, length = 64)
    private String role;

    @Column(name = "issued_at", nullable = false)
    private Instant issuedAt;

    @Column(name = "expires_at", nullable = false)
    private Instant expiresAt;

    @Column(name = "revoked_at")
    private Instant revokedAt;

    public JwtTokenEntity() {}

    public String getJti() { return jti; }
    public UUID getDeviceId() { return deviceId; }
    public String getRole() { return role; }
    public Instant getIssuedAt() { return issuedAt; }
    public Instant getExpiresAt() { return expiresAt; }
    public Instant getRevokedAt() { return revokedAt; }

    public void setJti(String jti) { this.jti = jti; }
    public void setDeviceId(UUID deviceId) { this.deviceId = deviceId; }
    public void setRole(String role) { this.role = role; }
    public void setIssuedAt(Instant issuedAt) { this.issuedAt = issuedAt; }
    public void setExpiresAt(Instant expiresAt) { this.expiresAt = expiresAt; }
    public void setRevokedAt(Instant revokedAt) { this.revokedAt = revokedAt; }
}