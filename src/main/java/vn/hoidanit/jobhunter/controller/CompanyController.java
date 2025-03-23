package vn.hoidanit.jobhunter.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.dto.PaginationResultDTO;
import vn.hoidanit.jobhunter.response.ResponseFactory;
import vn.hoidanit.jobhunter.response.RestResponse;
import vn.hoidanit.jobhunter.service.CompanyService;

@RestController
@RequestMapping("/api/v1/companies")
public class CompanyController {

    private final CompanyService companyService;

    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping
    public ResponseEntity<RestResponse<Company>> createCompany(@Valid @RequestBody Company company) {
        Company newCompany = this.companyService.create(company);
        return ResponseFactory.success(newCompany, "Company is created successfully", HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<RestResponse<PaginationResultDTO>> getAllCompanies(
            @Filter Specification<Company> specification,
            Pageable pageable) {

        PaginationResultDTO companies = this.companyService.findAll(specification, pageable);
        return ResponseFactory.success(companies, "Get all companies successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestResponse<Company>> getCompany(@PathVariable(name = "id") final Long id) {
        Company company = this.companyService.findOne(id);
        return ResponseFactory.success(company, "Get company successfully");
    }

    @PutMapping()
    public ResponseEntity<RestResponse<Company>> updateCompany(
            @Valid @RequestBody Company company) {
        Company updatedCompany = this.companyService.update(company);
        return ResponseFactory.success(updatedCompany, "Update company successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RestResponse<Void>> deleteCompany(@PathVariable(name = "id") long id) {
        this.companyService.delete(id);
        return ResponseFactory.noContent("Delete company successfully");
    }
}
