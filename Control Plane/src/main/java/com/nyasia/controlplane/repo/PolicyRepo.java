package com.nyasia.controlplane.repo;

import com.nyasia.controlplane.model.PolicyEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PolicyRepo extends JpaRepository <PolicyEntity, String>{
}
