package wfm.tenant.ControlCenter.repository;

import io.micrometer.common.lang.NonNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import wfm.tenant.ControlCenter.entity.Localeenabled;
import wfm.tenant.ControlCenter.entity.LocaleenabledId;

import java.util.List;
import java.util.UUID;

@Repository
public interface LocaleEnabledRepository extends JpaRepository<Localeenabled, LocaleenabledId> {

    @Query("SELECT l.locale.localeId FROM Localeenabled l WHERE l.tenant.id = :tenantId")
    List<String> findLocalesByTenantId(@Param("tenantId") UUID tenantId);

    boolean existsById(@NonNull LocaleenabledId id);
}
