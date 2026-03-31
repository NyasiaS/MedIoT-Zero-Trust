// Gateway/src/main/java/com/nyasia/gateway/core/PolicyJson.java
package com.nyasia.gateway.core;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashSet;
import java.util.Set;

public final class PolicyJson {
    private static final ObjectMapper MAPPER = new ObjectMapper();

    private PolicyJson() {}

    public static Set<String> parseAllowList(String policyJson) {
        try {
            JsonNode root = MAPPER.readTree(policyJson);
            JsonNode allow = root.get("allow");
            Set<String> out = new HashSet<>();
            if (allow != null && allow.isArray()) {
                for (JsonNode n : allow) out.add(n.asText());
            }
            return out;
        } catch (Exception e) {
            return Set.of();
        }
    }
}
