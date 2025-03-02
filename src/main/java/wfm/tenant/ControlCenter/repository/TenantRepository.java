package wfm.tenant.ControlCenter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import wfm.tenant.ControlCenter.entity.Tenant;
import wfm.tenant.ControlCenter.projection.TenantProjection;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface TenantRepository extends JpaRepository<Tenant, UUID> {

    @Query("SELECT t FROM Tenant t")
    Optional<Tenant> findByTenantName(String tenantName);

    @Query("SELECT t FROM Tenant t LEFT JOIN FETCH t.localeDefault")
    List<TenantProjection> findAllTenants();

    @Query("SELECT t FROM Tenant t WHERE t.customer.id = :customerId")
    List<TenantProjection> getTenantsByCustomer(@Param("customerId") Long customerId);
}
