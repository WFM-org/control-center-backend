package ControlCenter.service;

import ControlCenter.dto.FieldOverrideDTO;
import ControlCenter.entity.FieldOverride;
import ControlCenter.entity.FieldOverrideId;
import ControlCenter.repository.AllowedFieldOverrideRepository;
import ControlCenter.repository.FieldOverrideRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FieldOverrideService {

    private final FieldOverrideRepository fieldOverrideRepository;
    private final AllowedFieldOverrideRepository allowedFieldOverrideRepository;

    public List<FieldOverrideDTO> getOverridesForTenant(UUID tenantId) {
        var overrides = fieldOverrideRepository.findByTenantId(tenantId).stream()
                .collect(Collectors.toMap(FieldOverride::getAllowedFieldOverrideId, o -> o));

        return allowedFieldOverrideRepository.findAll().stream()
                .map(allowed -> {
                    var override = overrides.get(allowed.getId());
                    return new FieldOverrideDTO(
                            allowed.getTableName(),
                            allowed.getFieldName(),
                            override != null && override.getVisible(),
                            override != null && override.getMandatory(),
                            override != null && override.getEditable()
                    );
                })
                .toList();
    }

    @Transactional
    public void updateOverride(UUID tenantId, FieldOverrideDTO dto) {
        var allowed = allowedFieldOverrideRepository
                .findByTableNameAndFieldName(dto.getTableName(), dto.getFieldName());

        if (allowed == null)
            throw new IllegalArgumentException("Ukendt felt: " + dto.getTableName() + "." + dto.getFieldName());

        var id = new FieldOverrideId(tenantId, allowed.getId());
        var override = fieldOverrideRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Override ikke fundet"));

        override.setVisible(dto.isVisible());
        override.setMandatory(dto.isMandatory());
        override.setEditable(dto.isEditable());

        fieldOverrideRepository.save(override);
    }
}