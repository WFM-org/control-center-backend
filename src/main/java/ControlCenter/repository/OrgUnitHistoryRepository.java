package ControlCenter.repository;

import ControlCenter.entity.OrgUnitHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface OrgUnitHistoryRepository extends JpaRepository<OrgUnitHistory, UUID> {
}
