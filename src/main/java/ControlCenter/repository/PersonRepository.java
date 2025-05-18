package ControlCenter.repository;

import ControlCenter.entity.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface PersonRepository extends JpaRepository<Person, UUID> {
    @Query("SELECT p FROM Person p " +
            "JOIN FETCH p.personHistories ph " +
            "WHERE p.internalId = :internalId")
    Optional<Person> findPersonById(@Param("internalId") UUID internalId);

    @Query("SELECT p FROM Person p " +
            "JOIN FETCH p.personHistories ph " +
            "WHERE p.tenant = :tenantId")
    List<Person> findPersonsByTenantId(@Param("tenantId") UUID tenantId);

    @Query("SELECT p FROM Person p " +
            "JOIN FETCH p.personHistories ph " +
            "WHERE ph.firstName = :firstName")
    List<Person> findPersonsByFirstName(@Param("firstName") String firstName);
}

