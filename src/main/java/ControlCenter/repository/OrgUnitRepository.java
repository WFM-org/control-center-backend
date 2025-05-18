package ControlCenter.repository;

import ControlCenter.entity.OrgUnit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrgUnitRepository extends JpaRepository<OrgUnit, UUID> {

    @Query("SELECT o FROM OrgUnit o " +
            "JOIN FETCH o.orgUnitHistories oh " +
            "WHERE o.internalId = :internalId")
    Optional<OrgUnit> findOrgUnitById(@Param("internalId") UUID internalId);

    @Query("SELECT o FROM OrgUnit o " +
            "JOIN FETCH o.orgUnitHistories oh " +
            "WHERE o.tenant = :tenantId")
    List<OrgUnit> findOrgUnitsByTenantId(@Param("tenantId") UUID tenantId);

    @Query("SELECT o FROM OrgUnit o " +
            "JOIN FETCH o.orgUnitHistories oh " +
            "WHERE oh.name = :name")
    List<OrgUnit> findOrgUnitsByName(@Param("name") String name);
}
