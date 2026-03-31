// Gateway/src/main/java/com/nyasia/gateway/api/GatewayController.java
package com.nyasia.gateway.api;

import com.nyasia.gateway.core.GatewayService;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/gw")
public class GatewayController {

    private final GatewayService gatewayService;

    public GatewayController(GatewayService gatewayService) {
        this.gatewayService = gatewayService;
    }

    @PostMapping("/events")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void ingest(@AuthenticationPrincipal Jwt jwt, @RequestBody GatewayEventRequest req) {
        gatewayService.ingest(jwt, req.action(), req.payload());
    }

    public record GatewayEventRequest(String action, String payload) {}
}