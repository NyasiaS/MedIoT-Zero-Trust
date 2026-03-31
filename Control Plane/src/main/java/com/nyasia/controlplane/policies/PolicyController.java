// Control Plane/src/main/java/com/nyasia/controlplane/policies/PolicyController.java
package com.nyasia.controlplane.policies;

import com.nyasia.controlplane.model.PolicyEntity;
import com.nyasia.controlplane.repo.PolicyRepo;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;

@RestController
@RequestMapping("/policies")
public class PolicyController {

    private final PolicyRepo policies;

    public PolicyController(PolicyRepo policies) {
        this.policies = policies;
    }

    @GetMapping("/{role}")
    public PolicyEntity get(@PathVariable String role) {
        return policies.findById(role).orElse(null);
    }

    @PutMapping("/{role}")
    public PolicyEntity upsert(@PathVariable String role, @RequestBody PolicyEntity body) {
        body.setRole(role);
        body.setUpdatedAt(Instant.now());
        return policies.save(body);
    }
}
