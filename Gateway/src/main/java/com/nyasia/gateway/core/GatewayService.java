// Gateway/src/main/java/com/nyasia/gateway/core/GatewayService.java
package com.nyasia.gateway.core;

import com.nyasia.gateway.core.ControlPlaneAuthClient.IntrospectResponse;
import org.springframework.stereotype.Service;
import org.springframework.security.oauth2.jwt.Jwt;

import java.util.Set;
import java.util.UUID;

@Service
public class GatewayService {

    private final ControlPlaneClient controlPlaneClient;
    private final ControlPlaneAuthClient authClient;

    public GatewayService(ControlPlaneClient controlPlaneClient, ControlPlaneAuthClient authClient) {
        this.controlPlaneClient = controlPlaneClient;
        this.authClient = authClient;
    }

    public void ingest(Jwt jwt, String action, String payload) {
        UUID deviceId = UUID.fromString(jwt.getSubject());
        String roleFromJwt = jwt.getClaimAsString("role");

        IntrospectResponse live = authClient.introspect(deviceId);
        if (!live.enabled()) {
            throw new Unauthorized("Device disabled");
        }

        // If role changed in CP, prefer CP role (prevents stale/forged roles if key rotated)
        String effectiveRole = (live.role() != null && !live.role().isBlank()) ? live.role() : roleFromJwt;

        Policy policy = controlPlaneClient.getPolicy(effectiveRole);
        boolean allowed = policy.allowedActions().contains(action);

        controlPlaneClient.forwardEvent(new ControlPlaneEvent(
                deviceId.toString(),
                effectiveRole,
                action,
                allowed ? "ALLOWED" : "BLOCKED",
                payload
        ));
    }

    public record Policy(String role, Set<String> allowedActions) {}
    public record ControlPlaneEvent(String deviceId, String role, String action, String outcome, String payload) {}

    @SuppressWarnings("serial")
    public static class Unauthorized extends RuntimeException {
        public Unauthorized(String msg) { super(msg); }
    }
}