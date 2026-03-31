// Control Plane/src/main/java/com/nyasia/controlplane/model/PolicyEntity.java
package com.nyasia.controlplane.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "policies")
public class PolicyEntity {

    @Id
    @Column(name = "role", nullable = false, length = 64)
    private String role;

    @Column(name = "policy_json", nullable = false, length = 8192)
    private String policyJson = "{}";

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    public PolicyEntity() {}

    @PrePersist
    private void prePersist() {
        if (updatedAt == null) updatedAt = Instant.now();
        if (policyJson == null) policyJson = "{}";
    }

    public String getRole() { return role; }
    public String getPolicyJson() { return policyJson; }
    public Instant getUpdatedAt() { return updatedAt; }

    public void setRole(String role) { this.role = role; }
    public void setPolicyJson(String policyJson) { this.policyJson = policyJson; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }
}
