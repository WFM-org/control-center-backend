package ControlCenter.service;

import ControlCenter.dto.CompanyDTO;
import ControlCenter.dto.CompanyHistoryDTO;
import ControlCenter.dto.LanguagePackDTO;
import ControlCenter.entity.Company;
import ControlCenter.entity.CompanyHistory;
import ControlCenter.entity.LanguagePack;
import ControlCenter.exception.*;
import ControlCenter.fieldValidators.ImmutableFieldValidation;
import ControlCenter.repository.CompanyHistoryRepository;
import ControlCenter.repository.CompanyRepository;
import ControlCenter.service.utils.ServiceUtils;
import jakarta.persistence.EntityManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final CompanyHistoryRepository companyHistoryRepository;
    private final LanguagePackService languagePackService;
    private final EntityManager entityManager;

    public CompanyService(CompanyRepository companyRepository,
                          CompanyHistoryRepository companyHistoryRepository,
                          LanguagePackService languagePackService,
                          EntityManager entityManager) {
        this.companyRepository = companyRepository;
        this.companyHistoryRepository = companyHistoryRepository;
        this.languagePackService = languagePackService;
        this.entityManager = entityManager;
    }

    public Optional<CompanyDTO> getCompanyByInternalId(UUID internalId, LocalDate effectiveDate) {
        Optional<Company> company = companyRepository.findCompanyById(internalId);
        return company.map(value -> CompanyDTO.fromEntity(value, effectiveDate));
    }

    public List<CompanyDTO> getCompaniesByTenant(UUID tenantId, LocalDate effectiveDate) {
        return companyRepository.findCompaniesByTenantId(tenantId)
                .stream()
                .map(value -> CompanyDTO.fromEntity(value, effectiveDate))
                .toList();
    }

    @Transactional
    public void deleteCompany(UUID internalId) throws CompanyNotFoundException {
        Optional<Company> company = companyRepository.findCompanyById(internalId);
        if(company.isPresent()) {
            companyRepository.delete(company.get());
        } else {
            log.error("Company with ID {} could not be deleted because " +
                    "it was not found in the database.", internalId);
            throw new CompanyNotFoundException();
        }
    }

    @Transactional
    public CompanyDTO createCompany(CompanyDTO company) throws TenantNotFoundException, CompanyControlUnknownError {
        // Company insert
        Company companyEntity = Company.fromDTO(company);
        Company newCompanyEntity = companyRepository.save(companyEntity);

        // Historical record insert
        LanguagePack languagePack = languagePackService.getDefaultLanguagePackByTenantId(newCompanyEntity.getTenant());
        LanguagePackDTO languagePackDTO = LanguagePackDTO.fromEntity(languagePack);
        CompanyHistoryDTO companyHistoryDTO = new CompanyHistoryDTO(newCompanyEntity.getInternalId(),
                company.getStartDate(), company.getName(), languagePackDTO, company.getTimezone());
        CompanyHistory companyHistoryEntity = CompanyHistory.fromDTO(companyHistoryDTO);
        List<CompanyHistory> mutableCompanyHistoryList = new ArrayList<>(List.of(companyHistoryEntity));
        newCompanyEntity.setCompanyHistories(mutableCompanyHistoryList);
        Company inserted = companyRepository.save(newCompanyEntity);

        entityManager.flush();
        entityManager.refresh(inserted);

        log.info("Successfully created company with ID: {} and external ID: {}",
                inserted.getInternalId(), inserted.getExternalId());
        return CompanyDTO.fromEntity(inserted, company.getStartDate());
    }

    @Transactional
    public CompanyDTO updateCompany(UUID internalId, LocalDate effectiveDate, CompanyDTO update) throws CompanyNotFoundException, ImmutableUpdateException, CompanyHistoryNotFoundException {
        // Company update
        Company toUpdate = companyRepository.findCompanyById(internalId)
                .orElseThrow(CompanyNotFoundException::new);

        Company updateFrom = Company.fromDTO(update);
        ImmutableFieldValidation.validate(updateFrom, toUpdate);
        BeanUtils.copyProperties(updateFrom, toUpdate, ServiceUtils.getNullPropertyNames(updateFrom));
        Company newCompany = companyRepository.save(toUpdate);

        // Historical record update
        CompanyHistoryDTO newCompanyHistory = new CompanyHistoryDTO();

        if(effectiveDate != null) {
            // Existing historical record:
            // Step 1 - Find the original historical record
            // Step 2 - Delete the original historical record and allow triggers to shift historical records in the database
            CompanyHistory history = newCompany.getCompanyHistories().stream()
                    .filter(f -> f.getId().getStartDate().equals(effectiveDate))
                    .findFirst().orElseThrow(CompanyHistoryNotFoundException::new);

            newCompanyHistory = CompanyHistoryDTO.fromEntity(history);

            companyHistoryRepository.deleteByEmbeddedId(history.getId());
            newCompany.getCompanyHistories().remove(history);

        }

        CompanyHistoryDTO updateCompanyHistoryDTO = new CompanyHistoryDTO(newCompany.getInternalId(),
                update.getStartDate(), update.getName(), update.getLanguagePackDefault(), update.getTimezone());
        BeanUtils.copyProperties(updateCompanyHistoryDTO, newCompanyHistory, ServiceUtils.getNullPropertyNames(updateCompanyHistoryDTO));
        newCompany.getCompanyHistories().add(CompanyHistory.fromDTO(newCompanyHistory));
        companyRepository.save(newCompany);
        entityManager.flush();
        entityManager.refresh(newCompany);

        log.info("Successfully updated company with ID: {}", newCompany.getInternalId());
        return CompanyDTO.fromEntity(newCompany, newCompanyHistory.getStartDate());
    }

    @Transactional
    public CompanyHistoryDTO createCompanyHistoricalRecord(UUID internalId, CompanyHistoryDTO record) throws CompanyNotFoundException {
        Company company = companyRepository.findCompanyById(internalId)
                .orElseThrow(CompanyNotFoundException::new);

        // Historical record insert
        Optional<CompanyHistory> exist = company.getCompanyHistories().stream()
                .filter(f -> f.getId().getStartDate().equals(record.getStartDate()))
                .findFirst();
        if(exist.isPresent()) {
            companyHistoryRepository.deleteByEmbeddedId(exist.get().getId());
            entityManager.flush();
            entityManager.refresh(company);
        }

        CompanyHistoryDTO newCompanyHistoryDTO = new CompanyHistoryDTO(company.getInternalId(),
                record.getStartDate(), record.getName(), record.getLanguagePackDefault(), record.getTimezone());

        CompanyHistory newCompanyHistory = CompanyHistory.fromDTO(newCompanyHistoryDTO);
        company.getCompanyHistories().add(newCompanyHistory);
        companyRepository.save(company);

        log.info("Successfully inserted historical record for company with ID: {}", company.getInternalId());
        return newCompanyHistoryDTO;
    }

    @Transactional
    public CompanyHistoryDTO deleteCompanyHistoricalRecord(UUID internalId, CompanyHistoryDTO record) throws CompanyNotFoundException, CompanyHistoryNotFoundException {
        Company company = companyRepository.findCompanyById(internalId)
                .orElseThrow(CompanyNotFoundException::new);

        // Historical record insert
        CompanyHistory exist = company.getCompanyHistories().stream()
                .filter(f -> f.getId().getStartDate().equals(record.getStartDate()))
                .findFirst().orElseThrow(CompanyHistoryNotFoundException::new);

        if(company.getCompanyHistories().size() == 1) {
            companyRepository.delete(company);
            log.info("Successfully deleted last historical record and company with ID: {}", company.getInternalId());
        } else {
            companyHistoryRepository.deleteByEmbeddedId(exist.getId());
            log.info("Successfully deleted historical record for company with ID: {}", company.getInternalId());
        }

        return CompanyHistoryDTO.fromEntity(exist);
    }
}
