package ControlCenter.repository;

import ControlCenter.entity.CompanyHistory;
import ControlCenter.entity.compositeKey.CompanyHistoryId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Repository
public interface CompanyHistoryRepository extends JpaRepository<CompanyHistory, UUID> {
}
