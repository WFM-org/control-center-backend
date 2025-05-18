package ControlCenter.repository;

import ControlCenter.entity.EmploymentHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface EmploymentHistoryRepository extends JpaRepository<EmploymentHistory, UUID> {
}
