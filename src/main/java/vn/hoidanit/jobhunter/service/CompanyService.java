package vn.hoidanit.jobhunter.service;

import java.util.List;

import org.springframework.data.domain.Pageable;

import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.dto.PaginationResultDTO;

public interface CompanyService {

    Company findOne(long id);

    Company create(Company company);

    PaginationResultDTO findAll(Pageable pageable);

    Company update(Company company);

    void delete(long id);
}
