package vn.hoidanit.jobhunter.service;

import java.util.List;

import vn.hoidanit.jobhunter.domain.Company;

public interface CompanyService {

    Company findOne(long id);

    Company create(Company company);

    List<Company> findAll();

    Company update(Company company);

    void delete(long id);
}
