package ControlCenter.service;

import ControlCenter.dto.CompanyDTO;
import ControlCenter.dto.CompanyHistoryDTO;
import ControlCenter.entity.Company;
import ControlCenter.entity.CompanyHistory;
import ControlCenter.entity.LanguagePack;
import ControlCenter.exception.CompanyControlUnknownError;
import ControlCenter.exception.CompanyNotFoundException;
import ControlCenter.exception.TenantNotFoundException;
import ControlCenter.projection.LanguagePackProjection;
import ControlCenter.repository.CompanyRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jdk.jshell.spi.ExecutionControl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpServerErrorException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final LanguagePackService languagePackService;

    @PersistenceContext
    private EntityManager entityManager;

    public CompanyService(CompanyRepository companyRepository, LanguagePackService languagePackService) {
        this.companyRepository = companyRepository;
        this.languagePackService = languagePackService;
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
        LanguagePackProjection languagePackProjection = LanguagePackProjection.mapToLanguagePackProjection(languagePack);
        CompanyHistoryDTO companyHistoryDTO = new CompanyHistoryDTO(newCompanyEntity.getInternalId(),
                company.getStartDate(), company.getName(), languagePackProjection, company.getTimezone());
        CompanyHistory companyHistoryEntity = CompanyHistory.fromDTO(companyHistoryDTO);
        newCompanyEntity.getCompanyHistories().add(companyHistoryEntity);
        Company inserted = companyRepository.save(newCompanyEntity);

        entityManager.flush();
        entityManager.refresh(inserted);

        log.info("Successfully created company with ID: {} and external ID: {}",
                inserted.getInternalId(), inserted.getExternalId());
        return CompanyDTO.fromEntity(inserted, company.getStartDate());
    }

    public List<CompanyDTO> getCompaniesByName(String companyName, LocalDate effectiveDate) {
        return companyRepository.findCompaniesByName(companyName).stream()
                .map(value -> CompanyDTO.fromEntity(value, effectiveDate))
                .toList();
    }

    public CompanyDTO updateCompany(UUID internalId, CompanyDTO update) throws CompanyNotFoundException {
        // Company update
        Optional<Company> company = companyRepository.findCompanyById(internalId);
        if(company.isEmpty()) {
            throw new CompanyNotFoundException();
        }

        // to scenarier der skal håndteres her
        // 1) vi opdaterer en historisk company, dvs input fra UI er effective dated,
        // 2) vi opdaterer den senest aktive historiske company, altså hvor end date ikke er imødekommet - dvs vi skal oprette en ny history
        LocalDate effectiveDate = update.getStartDate();


        // Historical record update - add new with effective date and make current effective one non-effective
        return null;
    }

    public CompanyHistoryDTO createCompanyHistoricalRecord(UUID internalId, CompanyHistoryDTO request) {
        return null;
    }

//    @Transactional
//    public Company createCompany(String externalId, String companyName, UUID tenantId) throws TenantNotFoundException {
//        validateNotBlank(externalId, "Company external Id");
//        validateNotBlank(companyName, "Company name");
//        validateNotBlank(tenantId.toString(), "Tenant Internal Id");
//
//        Company company = new Company();
//        company.setTenant(tenantId);
//        company.setExternalId(externalId);
//        company.setName(companyName);
//        company.setRecordStatus(RecordStatus.ACTIVE.getValue());
//        company.setLanguagePackDefault(languagePackService.getDefaultLanguagePackByTenantId(tenantId));
//
//        Company created = companyRepository.saveAndFlush(company);
//        log.info("Successfully created company with ID: {}", created.getId());
//        return created;
//    }
//
//    public CompanyProjection deleteCompanyById(UUID companyId) throws CompanyNotFoundException {
//        Optional<Company> byId = companyRepository.findById(companyId);
//        if(byId.isPresent()) {
//            Company company = byId.get();
//            company.setRecordStatus(RecordStatus.INACTIVE.getValue());
//
//            companyRepository.save(company);
//            CompanyProjection deleted = companyRepository.findCompanyById(companyId);
//            log.info("Successfully deleted company with ID: {}", companyId);
//            return deleted;
//        } else {
//            log.error("Company with ID {} could not be deleted because " +
//                    "it was not found in the database.", companyId);
//            throw new CompanyNotFoundException();
//        }
//    }
//
//    public CompanyProjection updateCompany(Company newCompany, UUID companyId) throws CompanyNotFoundException, ImmutableUpdateException {
//        Optional<Company> byId = companyRepository.findById(companyId);
//        if(byId.isPresent()) {
//            Company company = byId.get();
//            ImmutableFieldValidation.validate(newCompany, company);
//            BeanUtils.copyProperties(newCompany, company, ServiceUtils.getNullPropertyNames(newCompany));
//
//            companyRepository.save(company);
//            CompanyProjection saved = companyRepository.findCompanyById(companyId);
//            log.info("Successfully updated company with ID: {}", companyId);
//            return saved;
//        } else {
//            log.error("Company with ID {} could not be updated because " +
//                    "it was not found in the database.", companyId);
//            throw new CompanyNotFoundException();
//        }
//    }
//
}
