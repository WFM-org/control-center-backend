package ControlCenter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ControlCenter.entity.Company;
import ControlCenter.projection.CompanyProjection;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface CompanyRepository extends JpaRepository<Company, UUID> {


//    @Query("SELECT c FROM Company c")
//    List<CompanyProjection> findAllCompanies();
//
//    @Query("SELECT c FROM Company c WHERE c.id = :id")
//    CompanyProjection findCompanyById(UUID id);

    @Query("SELECT c FROM Company c " +
            "JOIN FETCH c.companyHistories ch " +
            "WHERE c.internalId = :internalId AND :effectiveDate BETWEEN ch.id.startDate AND ch.endDate")
    Optional<CompanyProjection> findCompanyByInternalId(@Param("internalId") UUID internalId,
                                                        @Param("effectiveDate") LocalDate effectiveDate);

//    @Query("SELECT c FROM Company c JOIN c.company_history ch WHERE LOWER(ch.name) = LOWER(:companyName)")
//    List<CompanyProjection> findCompaniesByCompanyName(String companyName);
//
//    @Modifying
//    @Query(value = "UPDATE Company SET externalId = :externalId, name = :name WHERE id = :id", nativeQuery = true)
//    void updateExternalId(@Param("externalId") String externalId, @Param("name") String name);


}
