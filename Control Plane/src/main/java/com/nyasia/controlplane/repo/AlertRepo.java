// Control Plane/src/main/java/com/nyasia/controlplane/repo/AlertRepo.javapackage com.nyasia.controlplane.repo;
package com.nyasia.controlplane.repo;

import com.nyasia.controlplane.model.AlertEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AlertRepo extends JpaRepository<AlertEntity, Long> {

    Optional<AlertEntity> findTopByDeviceIdAndTypeAndStatusOrderByTsDesc(
            String deviceId,
            String type,
            AlertEntity.Status status
    );
}