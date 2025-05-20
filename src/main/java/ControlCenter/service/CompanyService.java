package ControlCenter.service;

import ControlCenter.builder.HistoricalDataManagementBuilder;
import ControlCenter.dto.CompanyDTO;
import ControlCenter.dto.CompanyHistoryDTO;
import ControlCenter.entity.Company;
import ControlCenter.entity.CompanyHistory;
import ControlCenter.exception.*;
import ControlCenter.repository.CompanyHistoryRepository;
import ControlCenter.repository.CompanyRepository;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class CompanyService {

    private final HistoricalDataManagementBuilder<Company, CompanyHistory, CompanyDTO, CompanyHistoryDTO> builder;

    public CompanyService(CompanyRepository companyRepository,
                          CompanyHistoryRepository companyHistoryRepository,
                          EntityManager entityManager) {

        this.builder = new HistoricalDataManagementBuilder<>(
                companyRepository::findCompanyById,
                companyRepository::findCompaniesByTenantId,
                (dto, id) -> new CompanyHistoryDTO(null, id,
                        dto.getStartDate(), dto.getName(), dto.getCountry(), dto.getLanguagePackDefault(), dto.getTimezone()),
                (dto, id) -> new CompanyHistoryDTO(null, id,
                        dto.getStartDate(), dto.getName(), dto.getCountry(), dto.getLanguagePackDefault(), dto.getTimezone()),
                companyRepository::save,
                companyHistoryRepository::save,
                companyRepository::delete,
                companyHistoryRepository::delete,
                Company::getCompanyHistories,
                CompanyDTO::getStartDate,
                CompanyHistory::getStartDate,
                CompanyHistoryDTO::getStartDate,
                Company::getInternalId,
                Company::fromDTO,
                CompanyDTO::fromEntity,
                CompanyHistory::fromDTO,
                CompanyHistoryDTO::fromEntity,
                CompanyHistoryDTO::new,
                (ch, c) -> c.addCompanyHistory(ch),
                (ch) -> ch.setInternalId(null),
                entityManager
        );
    }

    public Optional<CompanyDTO> getCompanyByInternalId(UUID internalId, LocalDate effectiveDate) {
        return builder.readById(internalId, effectiveDate);
    }

    public List<CompanyDTO> getCompaniesByTenant(UUID tenantId, LocalDate effectiveDate) {
        return builder.readByTenant(tenantId, effectiveDate);
    }

    public void deleteCompany(UUID internalId) throws CompanyNotFoundException {
        try {
            builder.deleteEntity(internalId);
        } catch (EntityNotFoundException e) {
            throw new CompanyNotFoundException();
        }
    }

    @Transactional
    public CompanyDTO createCompany(CompanyDTO company) throws CompanyNotSavedException {
        return builder.createEntity(company);
    }

    @Transactional
    public CompanyDTO updateCompany(UUID internalId, LocalDate effectiveDate, CompanyDTO update) throws CompanyHistoryNotFoundException, CompanyWithImmutableUpdateException, CompanyNotFoundException {
        try {
            return builder.updateEntity(internalId, effectiveDate, update);
        } catch (EntityNotFoundException e) {
            throw new CompanyNotFoundException();
        } catch (HistoricalEntityNotFoundException e) {
            throw new CompanyHistoryNotFoundException();
        } catch (ImmutableUpdateException e) {
            throw new CompanyWithImmutableUpdateException(e.getFieldNames());
        }
    }

    @Transactional
    public CompanyHistoryDTO createCompanyHistoricalRecord(UUID parentId, CompanyHistoryDTO record) throws CompanyNotFoundException, CompanyHistoryFoundException {
        try {
            return builder.createHistoricalRecord(parentId, record);
        } catch (EntityNotFoundException e) {
            throw new CompanyNotFoundException();
        } catch (HistoricalEntityAlreadyExistException e) {
            throw new CompanyHistoryFoundException();
        }
    }

    @Transactional
    public Boolean deleteCompanyHistoricalRecord(UUID parentId, CompanyHistoryDTO record) throws CompanyNotFoundException, CompanyHistoryNotFoundException {
        try {
            return builder.deleteHistoricalRecord(parentId, record);
        } catch (EntityNotFoundException e) {
            throw new CompanyNotFoundException();
        } catch (HistoricalEntityNotFoundException e) {
            throw new CompanyHistoryNotFoundException();
        }
    }
}
