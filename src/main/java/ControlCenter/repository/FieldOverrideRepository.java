package ControlCenter.repository;

import ControlCenter.entity.FieldOverride;
import ControlCenter.entity.FieldOverrideId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface FieldOverrideRepository extends JpaRepository<FieldOverride, FieldOverrideId> {
    List<FieldOverride> findByTenantId(UUID tenantId);
}
