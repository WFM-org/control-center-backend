package wfm.tenant.ControlCenter.service;

import org.springframework.stereotype.Service;
import wfm.tenant.ControlCenter.projection.CompanyProjection;
import wfm.tenant.ControlCenter.repository.CompanyRepository;

import java.util.List;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public List<CompanyProjection> getAllCompanyProjection() {
        return companyRepository.findAllCompanies();
    }

    public List<CompanyProjection> getCompanyByExternalId(String companyExternalId) {
        return companyRepository.findCompaniesByCompanyExternalId(companyExternalId);
    }

    public List<CompanyProjection> getCompanyByName(String companyName) {
        return companyRepository.findCompaniesByCompanyName(companyName);
    }
}
