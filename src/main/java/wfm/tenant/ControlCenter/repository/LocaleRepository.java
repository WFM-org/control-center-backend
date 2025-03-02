package wfm.tenant.ControlCenter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import wfm.tenant.ControlCenter.entity.Locale;

@Repository
public interface LocaleRepository extends JpaRepository<Locale, String> {

}
