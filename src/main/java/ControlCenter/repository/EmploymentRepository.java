package ControlCenter.repository;

import ControlCenter.entity.Employment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface EmploymentRepository extends JpaRepository<Employment, UUID> {

    @Query("SELECT e FROM Employment e " +
            "JOIN FETCH e.employmentHistories eh " +
            "WHERE e.internalId = :internalId")
    Optional<Employment> findEmploymentById(@Param("internalId") UUID internalId);

    @Query("SELECT e FROM Employment e " +
            "JOIN FETCH e.employmentHistories eh " +
            "WHERE e.tenant = :tenantId")
    Optional<Employment> findEmploymentsByTenantId(@Param("tenantId") UUID tenantId);

    @Query("SELECT e FROM Employment e " +
            "JOIN FETCH e.employmentHistories eh " +
            "WHERE eh.event = :eventCode")
    List<Employment> findEmploymentsByEvent(@Param("eventCode") Short eventCode);
}
