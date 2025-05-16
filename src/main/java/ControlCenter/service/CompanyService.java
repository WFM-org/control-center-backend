package ControlCenter.service;

import ControlCenter.crud.CrudBuilder;
import ControlCenter.dto.CompanyDTO;
import ControlCenter.dto.CompanyHistoryDTO;
import ControlCenter.entity.Company;
import ControlCenter.entity.CompanyHistory;
import ControlCenter.entity.compositeKey.CompanyHistoryId;
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

    private final CrudBuilder<Company, CompanyHistory, CompanyDTO, CompanyHistoryDTO, CompanyHistoryId> builder;

    public CompanyService(CompanyRepository companyRepository,
                          CompanyHistoryRepository companyHistoryRepository,
                          EntityManager entityManager) {

        this.builder = new CrudBuilder<>(
                companyRepository::findCompanyById,
                companyRepository::findCompaniesByTenantId,
                this::createHistoricalRecordFromCompanyDTOCallBack,
                this::createHistoricalRecordFromCompanyHistoryDTOCallBack,
                companyRepository::save,
                companyRepository::delete,
                companyHistoryRepository::deleteByEmbeddedId,
                Company::getCompanyHistories,
                this::setCompanyHistoriesCallBack,
                CompanyDTO::getStartDate,
                this::fetchStartDateFromCompanyHistoryDTOCallBack,
                CompanyHistoryDTO::getStartDate,
                CompanyHistory::getId,
                Company::getInternalId,
                Company::fromDTO,
                CompanyDTO::fromEntity,
                CompanyHistory::fromDTO,
                CompanyHistoryDTO::fromEntity,
                this::initializeEmptyHistoricalDTOCallBack,
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

    public CompanyDTO createCompany(CompanyDTO company) throws CompanyNotSavedException {
        return builder.createEntity(company);
    }

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

    public CompanyHistoryDTO createCompanyHistoricalRecord(UUID parentId, CompanyHistoryDTO record) throws CompanyNotFoundException, CompanyHistoryFoundException {
        try {
            return builder.createHistoricalRecord(parentId, record);
        } catch (EntityNotFoundException e) {
            throw new CompanyNotFoundException();
        } catch (HistoricalEntityAlreadyExistException e) {
            throw new CompanyHistoryFoundException();
        }
    }

    public Boolean deleteCompanyHistoricalRecord(UUID parentId, CompanyHistoryDTO record) throws CompanyNotFoundException, CompanyHistoryNotFoundException {
        try {
            return builder.deleteHistoricalRecord(parentId, record);
        } catch (EntityNotFoundException e) {
            throw new CompanyNotFoundException();
        } catch (HistoricalEntityNotFoundException e) {
            throw new CompanyHistoryNotFoundException();
        }
    }

    protected Company setCompanyHistoriesCallBack(List<CompanyHistory> histories, Company company) {
        company.setCompanyHistories(histories);
        return company;
    }

    protected CompanyHistoryDTO initializeEmptyHistoricalDTOCallBack() {
        return new CompanyHistoryDTO();
    }
    protected LocalDate fetchStartDateFromCompanyHistoryDTOCallBack(CompanyHistory companyHistory) {
        return companyHistory.getId().getStartDate();
    }

    protected CompanyHistoryDTO createHistoricalRecordFromCompanyDTOCallBack(CompanyDTO companyDTO, UUID internalId) {
        return new CompanyHistoryDTO(internalId,
                companyDTO.getStartDate(), companyDTO.getName(), companyDTO.getLanguagePackDefault(), companyDTO.getTimezone());
    }

    protected CompanyHistoryDTO createHistoricalRecordFromCompanyHistoryDTOCallBack(CompanyHistoryDTO companyHistoryDTO, UUID internalId) {
        return new CompanyHistoryDTO(internalId,
                companyHistoryDTO.getStartDate(), companyHistoryDTO.getName(), companyHistoryDTO.getLanguagePackDefault(), companyHistoryDTO.getTimezone());
    }
}
