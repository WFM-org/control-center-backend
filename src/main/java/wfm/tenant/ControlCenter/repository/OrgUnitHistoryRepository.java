package wfm.tenant.ControlCenter.repository;

import wfm.tenant.ControlCenter.entity.OrgUnit;
import wfm.tenant.ControlCenter.entity.OrgUnitHistory;
import wfm.tenant.ControlCenter.entity.OrgUnitHistoryId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrgUnitHistoryRepository extends JpaRepository<OrgUnitHistory, OrgUnitHistoryId> {
    List<OrgUnitHistory> findByOrgUnitOrderById_StartDateAsc(OrgUnit orgUnit);
}

