package ControlCenter.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ControlCenter.entity.LanguagePack;

@Repository
public interface LanguagePackRepository extends JpaRepository<LanguagePack, String> {

}
