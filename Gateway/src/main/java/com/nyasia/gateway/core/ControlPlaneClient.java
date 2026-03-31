// Gateway /src/main/java/com/nyasia/gateway/core/ControlPlaneClient.java
package com.nyasia.gateway.core;

import com.nyasia.gateway.core.GatewayService.ControlPlaneEvent;
import com.nyasia.gateway.core.GatewayService.Policy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;
import java.util.Set;

@Component
public class ControlPlaneClient {

    private final WebClient webClient;

    public ControlPlaneClient(@Value("${controlplane.base-url}") String baseUrl) {
        this.webClient = WebClient.builder().baseUrl(baseUrl).build();
    }

    public Policy getPolicy(String role) {
        // expects CP returns PolicyEntity with policyJson or something similar.
        // If your CP currently returns PolicyEntity directly, adapt parsing here.
        Map<String, Object> raw = webClient.get()
                .uri("/policies/{role}", role)
                .accept(MediaType.APPLICATION_JSON)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        if (raw == null) return new Policy(role, Set.of());

        Object pj = raw.get("policyJson");
        if (!(pj instanceof String s)) return new Policy(role, Set.of());

        // Very simple policyJson format expectation:
        // {"allow":["ping","telemetry"]}
        Set<String> allow = PolicyJson.parseAllowList(s);
        return new Policy(role, allow);
    }

    public void forwardEvent(ControlPlaneEvent event) {
        webClient.post()
                .uri("/events/ingest")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(event)
                .retrieve()
                .toBodilessEntity()
                .block();
    }
}
