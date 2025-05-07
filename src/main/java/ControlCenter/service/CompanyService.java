package ControlCenter.service;

import ControlCenter.entity.Company;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ControlCenter.projection.CompanyProjection;
import ControlCenter.repository.CompanyRepository;

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

//    public List<CompanyProjection> getAllCompanies() {
//        return companyRepository.findAllCompanies();
//    }

    public Optional<CompanyProjection> getCompanyByExternalId(UUID internalId, LocalDate effectiveDate) {
        return companyRepository.findCompanyByInternalId(internalId, effectiveDate);
    }

//    public List<CompanyProjection> getCompanyByName(String companyName) {
//        return companyRepository.findCompaniesByCompanyName(companyName);
//    }

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
//    private void validateNotBlank(String value, String fieldName) {
//        if (value == null || value.isBlank()) {
//            throw new IllegalArgumentException(fieldName + " can not be empty");
//        }
//    }
}
