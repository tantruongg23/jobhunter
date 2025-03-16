package vn.hoidanit.jobhunter.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.exception.IdInvalidException;
import vn.hoidanit.jobhunter.repository.CompanyRepository;
import vn.hoidanit.jobhunter.service.CompanyService;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyServiceImpl(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Override
    public Company create(Company company) {
        return this.companyRepository.save(company);
    }

    @Override
    public Company findOne(long id) {
        Optional<Company> existedCompany = this.companyRepository.findById(id);
        if (existedCompany.isEmpty()) {
            return null;
        }

        return existedCompany.get();

    }

    @Override
    public List<Company> findAll() {
        return this.companyRepository.findAll();
    }

    @Override
    public Company update(Company company) {
        Company currentCompany = this.findOne(company.getId());
        if (currentCompany == null) {
            return null;
        }

        currentCompany.setLogo(company.getLogo());
        currentCompany.setName(company.getName());
        currentCompany.setDescription(company.getDescription());
        currentCompany.setAddress(company.getAddress());

        return this.companyRepository.save(company);
    }

    @Override
    public void delete(long id) {
        this.companyRepository.deleteById(id);
    }

}
