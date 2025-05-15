package ControlCenter.dto;

import ControlCenter.entity.LanguagePack;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LanguagePackDTO {
    private String internalId;
    private String languageName;

    public static LanguagePackDTO fromEntity(LanguagePack languagePack) {
        return new LanguagePackDTO(languagePack.getInternalId(), languagePack.getLanguageName());
    }
}
