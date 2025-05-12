package ControlCenter.repository;

import ControlCenter.entity.AllowedFieldOverrides;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AllowedFieldOverrideRepository extends JpaRepository<AllowedFieldOverrides, Integer> {

    AllowedFieldOverrides findByTableNameAndFieldName(String tableName, String fieldName);
}