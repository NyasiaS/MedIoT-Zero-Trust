// Control Plane/src/main/java/com/nyasia/controlplane/repo/JwtTokenRepo.java
package com.nyasia.controlplane.repo;

import com.nyasia.controlplane.model.JwtTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JwtTokenRepo extends JpaRepository<JwtTokenEntity, String> {}