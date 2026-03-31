package com.nyasia.controlplane;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

@SpringBootApplication
public class ControlPlaneApplication {
    public static void main(String[] args) {
        SpringApplication.run(ControlPlaneApplication.class, args);
    }

    @Bean
    ApplicationRunner printRoutes(RequestMappingHandlerMapping mapping) {
        return args -> mapping.getHandlerMethods().forEach((info, method) -> {
            if (info.toString().contains("/alerts")) {
                System.out.println("REGISTERED: " + info);
            }
        });
    }
}
