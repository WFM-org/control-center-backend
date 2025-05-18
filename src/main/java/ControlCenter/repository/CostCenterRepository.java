package ControlCenter.repository;

import ControlCenter.entity.CostCenter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CostCenterRepository extends JpaRepository<CostCenter, UUID> {

    @Query("SELECT c FROM CostCenter c " +
            "JOIN FETCH c.costCenterHistories ch " +
            "WHERE c.internalId = :internalId")
    Optional<CostCenter> findCostCenterById(@Param("internalId") UUID internalId);

    @Query("SELECT c FROM CostCenter c " +
            "JOIN FETCH c.costCenterHistories ch " +
            "WHERE c.tenant = :tenantId")
    Optional<CostCenter> findCostCentersByTenantId(@Param("tenantId") UUID tenantId);

    @Query("SELECT c FROM CostCenter c " +
            "JOIN FETCH c.costCenterHistories ch " +
            "WHERE ch.name = :name")
    List<CostCenter> findCostCentersByName(@Param("name") String name);
}
