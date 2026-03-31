package com.nyasia.controlplane.repo;

import com.nyasia.controlplane.model.DeviceEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface DeviceRepo extends JpaRepository<DeviceEntity, UUID> {

    default Optional<DeviceEntity> findByIdSafe(String deviceId) {
        try {
            return findById(UUID.fromString(deviceId));
        } catch (Exception e) {
            return Optional.empty();
        }
    }
}