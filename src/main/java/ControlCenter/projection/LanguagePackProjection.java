package ControlCenter.projection;

import ControlCenter.entity.LanguagePack;

public interface LanguagePackProjection {
    String getInternalId();
    String getLanguageName();
    static LanguagePackProjection mapToLanguagePackProjection(LanguagePack languagePack) {
        return new LanguagePackProjection() {
            @Override
            public String getInternalId() {
                return languagePack.getInternalId();
            }
            @Override
            public String getLanguageName() {
                return languagePack.getLanguageName();
            }
        };
    }
}



