package vn.hoidanit.jobhunter.service.impl;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Company;

import vn.hoidanit.jobhunter.domain.dto.PaginationResultDTO;

import vn.hoidanit.jobhunter.repository.CompanyRepository;
import vn.hoidanit.jobhunter.repository.UserRepository;
import vn.hoidanit.jobhunter.service.CompanyService;
import vn.hoidanit.jobhunter.service.UserService;

@Service
public class CompanyServiceImpl implements CompanyService {

    private final CompanyRepository companyRepository;

    // private final UserService userService;
    private final UserRepository userRepository;

    public CompanyServiceImpl(CompanyRepository companyRepository,
            UserRepository userRepository) {
        this.companyRepository = companyRepository;
        // this.userService = userService;
        this.userRepository = userRepository;
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
    public PaginationResultDTO findAll(Specification<Company> spec, Pageable pageable) {
        Page<Company> pageCompanies = this.companyRepository.findAll(spec, pageable);
        PaginationResultDTO result = new PaginationResultDTO();
        PaginationResultDTO.Meta meta = new PaginationResultDTO.Meta();

        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(pageCompanies.getTotalPages());
        meta.setTotal(pageCompanies.getTotalElements());

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

        // Kiểm tra xem công ty có người dùng không
        long userCount = this.userRepository.countByCompanyId(id);
        // Nếu công ty có người dùng, throw exception và không xóa
        if (userCount > 0) {
            throw new IllegalStateException("Cannot delete company, there are users assigned to this company.");
        }

        this.companyRepository.deleteById(id);
    }

}
