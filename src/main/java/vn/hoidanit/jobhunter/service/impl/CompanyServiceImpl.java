package vn.hoidanit.jobhunter.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.dto.Meta;
import vn.hoidanit.jobhunter.domain.dto.PaginationResultDTO;
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
    public PaginationResultDTO findAll(Pageable pageable) {
        Page<Company> pageCompanies = this.companyRepository.findAll(pageable);
        PaginationResultDTO result = new PaginationResultDTO();
        Meta meta = new Meta();

        meta.setPage(pageCompanies.getNumber() + 1);
        meta.setPageSize(pageCompanies.getSize());
        meta.setPages(pageCompanies.getTotalPages());
        meta.setTotal(pageCompanies.getNumberOfElements());

        result.setMeta(meta);
        result.setResult(pageCompanies.getContent());

        return result;
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
