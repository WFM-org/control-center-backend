package ControlCenter.repository;

import ControlCenter.entity.OrgUnit;
import ControlCenter.entity.OrgUnitHistory;
import ControlCenter.entity.OrgUnitHistoryId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrgUnitHistoryRepository extends JpaRepository<OrgUnitHistory, OrgUnitHistoryId> {
    List<OrgUnitHistory> findByOrgUnitOrderById_StartDateAsc(OrgUnit orgUnit);
}

