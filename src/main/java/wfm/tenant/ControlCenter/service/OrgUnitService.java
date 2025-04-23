package wfm.tenant.ControlCenter.service;

import wfm.tenant.ControlCenter.dto.OrgUnitResponseDTO;
import wfm.tenant.ControlCenter.entity.OrgUnit;
import wfm.tenant.ControlCenter.entity.OrgUnitHistory;
import wfm.tenant.ControlCenter.repository.OrgUnitHistoryRepository;
import wfm.tenant.ControlCenter.repository.OrgUnitRepository;

import java.time.LocalDate;
import java.util.*;

public class OrgUnitService {

    OrgUnitRepository orgUnitRepository;
    OrgUnitHistoryRepository orgUnitHistoryRepository;

    public List<OrgUnitResponseDTO> getOrgUnits(LocalDate asOfDate, String statusFilter) {
        //TODO: get Tenant from JWT token
        UUID tenantId = UUID.randomUUID();
        List<OrgUnit> orgUnits = orgUnitRepository.findByTenant(tenantId);

        return orgUnits.stream().map(orgUnit -> {
            List<OrgUnitHistory> histories = orgUnitHistoryRepository.findByOrgUnitOrderById_StartDateAsc(orgUnit);

            Optional<OrgUnitHistory> current = histories.stream()
                    .filter(h -> !h.getId().getStartDate().isAfter(asOfDate) && !h.getEndDate().isBefore(asOfDate))
                    .findFirst();

            Optional<OrgUnitHistory> future = histories.stream()
                    .filter(h -> h.getId().getStartDate().isAfter(asOfDate))
                    .min(Comparator.comparing(h -> h.getId().getStartDate()));

            OrgUnitHistory relevant = current.orElse(future.orElse(null));
            if (relevant == null) return null;

            boolean isActive = current.isPresent();
            if ("active".equals(statusFilter) && !isActive) return null;
            if ("inactive".equals(statusFilter) && isActive) return null;

            return new OrgUnitResponseDTO(
                    orgUnit.getExternalId(),
                    relevant.getName(),
                    relevant.getRecordStatus(),
                    relevant.getId().getStartDate(),
                    relevant.getEndDate()
            );
        }).filter(Objects::nonNull).toList();
    }

}
