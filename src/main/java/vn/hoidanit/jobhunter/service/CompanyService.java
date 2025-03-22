package vn.hoidanit.jobhunter.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.dto.PaginationResultDTO;

public interface CompanyService {

    Company findOne(long id);

    Company create(Company company);

    PaginationResultDTO findAll(Specification<Company> spec, Pageable pageable);

    Company update(Company company);

    void delete(long id);

}
