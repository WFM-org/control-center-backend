package ControlCenter.service;

import ControlCenter.dto.CompanyDTO;
import ControlCenter.dto.CompanyHistoryDTO;
import ControlCenter.entity.Company;
import ControlCenter.entity.CompanyHistory;
import ControlCenter.entity.LanguagePack;
import ControlCenter.exception.CompanyNotFoundException;
import ControlCenter.exception.TenantNotFoundException;
import ControlCenter.repository.CompanyRepository;
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

    private final CompanyRepository companyRepository;
    private final LanguagePackService languagePackService;

    public CompanyService(CompanyRepository companyRepository, LanguagePackService languagePackService) {
        this.companyRepository = companyRepository;
        this.languagePackService = languagePackService;
    }

    public Optional<CompanyDTO> getCompanyByInternalId(UUID internalId, LocalDate effectiveDate) {
        Optional<Company> company = companyRepository.findCompanyById(internalId);
        return company.map(value -> CompanyDTO.fromEntity(value, effectiveDate));
    }

    public List<CompanyDTO> getCompaniesByTenant(UUID internalId, LocalDate effectiveDate) {
        return companyRepository.findCompaniesByTenantId(internalId)
                .stream()
                .map(value -> CompanyDTO.fromEntity(value, effectiveDate))
                .toList();
    }

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
    public CompanyDTO createCompany(CompanyDTO company) throws TenantNotFoundException {
        // Company insert
        Company companyEntity = Company.fromDTO(company);
        Company newCompanyEntity = companyRepository.save(companyEntity);

        // Historical record insert
        LanguagePack defaultLP = languagePackService.getDefaultLanguagePackByTenantId(newCompanyEntity.getTenant());
        CompanyHistoryDTO companyHistoryDTO = new CompanyHistoryDTO(newCompanyEntity.getInternalId(),
                LocalDate.now(), company.getName(), defaultLP, company.getTimezone());
        CompanyHistory companyHistoryEntity = CompanyHistory.fromDTO(companyHistoryDTO);
        newCompanyEntity.getCompanyHistories().add(companyHistoryEntity);
        Company newHistoricalRecord = companyRepository.save(newCompanyEntity);

        log.info("Successfully created company with ID: {}", newHistoricalRecord.getInternalId());
        return CompanyDTO.fromEntity(newHistoricalRecord, LocalDate.now());
    }

    public List<CompanyDTO> getCompaniesByName(String companyName, LocalDate effectiveDate) {
        return companyRepository.findCompaniesByName(companyName).stream()
                .map(value -> CompanyDTO.fromEntity(value, effectiveDate))
                .toList();
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
