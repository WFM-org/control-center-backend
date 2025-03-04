package wfm.tenant.ControlCenter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wfm.tenant.ControlCenter.entity.LanguagePack;

@Repository
public interface LanguagePackRepository extends JpaRepository<LanguagePack, String> {

}
