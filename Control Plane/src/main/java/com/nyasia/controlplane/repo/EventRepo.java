package com.nyasia.controlplane.repo;

import com.nyasia.controlplane.model.EventEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;

public interface EventRepo extends JpaRepository<EventEntity, Long> {
    List<EventEntity> findByDeviceIdAndTsAfter(String deviceId, Instant after);
    List<EventEntity> findTop200ByOrderByTsDesc();
}