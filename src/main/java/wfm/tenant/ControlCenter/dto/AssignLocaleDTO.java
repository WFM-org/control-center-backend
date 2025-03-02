package wfm.tenant.ControlCenter.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class AssignLocaleDTO {

    @NotNull(message = "Tenant ID cannot be null")
    private UUID id;

    @NotNull(message = "Locale ID cannot be null")
    private String localeId;
}

