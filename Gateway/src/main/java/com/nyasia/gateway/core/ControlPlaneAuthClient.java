// Gateway/src/main/java/com/nyasia/gateway/core/ControlPlaneAuthClient.java
package com.nyasia.gateway.core;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class ControlPlaneAuthClient {

    private final WebClient webClient;
    private final long ttlSeconds;

    private final ConcurrentHashMap<UUID, CacheEntry> cache = new ConcurrentHashMap<>();

    public ControlPlaneAuthClient(
            @Value("${controlplane.base-url}") String baseUrl,
            @Value("${gateway.introspect.ttl-seconds:60}") long ttlSeconds
    ) {
        this.webClient = WebClient.builder().baseUrl(baseUrl).build();
        this.ttlSeconds = ttlSeconds;
    }

    public IntrospectResponse introspect(UUID deviceId) {
        CacheEntry cached = cache.get(deviceId);
        Instant now = Instant.now();
        if (cached != null && cached.expiresAt.isAfter(now)) {
            return cached.value;
        }

        Map<String, Object> raw = webClient.get()
                .uri("/auth/introspect/{deviceId}", deviceId)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        boolean enabled = raw != null && Boolean.TRUE.equals(raw.get("enabled"));
        String role = raw != null && raw.get("role") != null ? raw.get("role").toString() : null;

        IntrospectResponse resp = new IntrospectResponse(enabled, role);
        cache.put(deviceId, new CacheEntry(resp, now.plusSeconds(ttlSeconds)));
        return resp;
    }

    public record IntrospectResponse(boolean enabled, String role) {}
    private record CacheEntry(IntrospectResponse value, Instant expiresAt) {}
}