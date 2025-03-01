package wfm.tenant.ControlCenter.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import wfm.tenant.ControlCenter.entity.Company;
import wfm.tenant.ControlCenter.projection.CompanyProjection;
import wfm.tenant.ControlCenter.repository.CompanyRepository;

import java.util.List;

@Slf4j
@Service
public class CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public List<CompanyProjection> getAllCompanies() {
        return companyRepository.findAllCompanies();
    }

    public List<CompanyProjection> getCompanyByExternalId(String companyExternalId) {
        return companyRepository.findCompaniesByCompanyExternalId(companyExternalId);
    }

    public List<CompanyProjection> getCompanyByName(String companyName) {
        return companyRepository.findCompaniesByCompanyName(companyName);
    }

    @Transactional
    public void createCompany(String externalId, String companyName) {
        try {
            Company company = new Company();
            company.setExternalId(externalId);
            company.setName(companyName);
            companyRepository.saveAndFlush(company);
            log.info("Company: {} is successfully created! ", company.getName());
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }
}
