package wfm.tenant.ControlCenter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import wfm.tenant.ControlCenter.entity.Company;
import wfm.tenant.ControlCenter.projection.CompanyProjection;

import java.util.List;
import java.util.UUID;

@Repository
public interface CompanyRepository extends JpaRepository<Company, UUID> {


    @Query("SELECT c FROM Company c")
    List<CompanyProjection> findAllCompanies();

    @Query("SELECT c FROM Company c WHERE c.id = :id")
    CompanyProjection findCompanyById(UUID id);

    @Query("SELECT c FROM Company c WHERE c.externalId = :externalId")
    List<CompanyProjection> findCompaniesByCompanyExternalId(@Param("externalId") String companyExternalId);

    @Query("SELECT c FROM Company c WHERE LOWER(c.name) = LOWER(:companyName)")
    List<CompanyProjection> findCompaniesByCompanyName(String companyName);

    @Modifying
    @Query(value = "UPDATE Company SET externalId = :externalId, name = :name WHERE id = :id", nativeQuery = true)
    void updateExternalId(@Param("externalId") String externalId, @Param("name") String name);


}
