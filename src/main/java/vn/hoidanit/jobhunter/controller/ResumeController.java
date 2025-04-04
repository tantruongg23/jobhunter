package vn.hoidanit.jobhunter.controller;

import java.util.List;
import java.util.stream.Collectors;

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
import org.springframework.web.bind.annotation.RestController;

import com.turkraft.springfilter.boot.Filter;
import com.turkraft.springfilter.builder.FilterBuilder;
import com.turkraft.springfilter.converter.FilterSpecificationConverter;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.Resume;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.dto.PaginationResultDTO;
import vn.hoidanit.jobhunter.domain.dto.response.ResResumeCreateDTO;
import vn.hoidanit.jobhunter.domain.dto.response.ResResumeDTO;
import vn.hoidanit.jobhunter.domain.dto.response.ResResumeUpdateDTO;
import vn.hoidanit.jobhunter.response.ResponseFactory;
import vn.hoidanit.jobhunter.response.RestResponse;
import vn.hoidanit.jobhunter.service.ResumeService;
import vn.hoidanit.jobhunter.service.UserService;
import vn.hoidanit.jobhunter.util.SecurityUtil;

@RestController
@RequestMapping("/api/v1/resumes")
public class ResumeController {
    private final ResumeService resumeService;
    private final UserService userService;

    private final FilterBuilder filterBuilder;
    private final FilterSpecificationConverter filterSpecificationConverter;

    public ResumeController(ResumeService resumeService, UserService userService, FilterBuilder filterBuilder,
            FilterSpecificationConverter filterSpecificationConverter) {
        this.resumeService = resumeService;
        this.userService = userService;
        this.filterBuilder = filterBuilder;
        this.filterSpecificationConverter = filterSpecificationConverter;

    }

    @PostMapping
    public ResponseEntity<RestResponse<ResResumeCreateDTO>> createResume(@Valid @RequestBody Resume resume) {
        ResResumeCreateDTO resResumeCreateDTO = this.resumeService.create(resume);
        return ResponseFactory.success(resResumeCreateDTO, "Create resume successfully", HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<RestResponse<ResResumeUpdateDTO>> updateResume(@RequestBody Resume resume) {
        ResResumeUpdateDTO resResumeUpdateDTO = this.resumeService.update(resume);
        return ResponseFactory.success(resResumeUpdateDTO, "Update resume successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RestResponse<Void>> deleteResume(@PathVariable(name = "id") long id) {
        this.resumeService.delete(id);
        return ResponseFactory.noContent("Delete resume successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestResponse<ResResumeDTO>> getResume(@PathVariable(name = "id") long id) {
        ResResumeDTO resResumeDTO = this.resumeService.findOne(id);
        return ResponseFactory.success(resResumeDTO, "Get resume successfully");
    }

    @GetMapping
    public ResponseEntity<RestResponse<PaginationResultDTO>> getAllResumes(@Filter Specification<Resume> specification,
            Pageable pageable) {

        List<Long> arrJobIds = null;
        String email = SecurityUtil.getCurrentUserLogin().orElse("");
        User currentUser = this.userService.findByEmail(email);
        if (currentUser != null) {
            Company userCompany = currentUser.getCompany();
            if (userCompany != null) {
                List<Job> companyJobs = userCompany.getJobs();
                if (companyJobs != null && companyJobs.size() > 0) {
                    arrJobIds = companyJobs
                            .stream()
                            .map(x -> x.getId())
                            .collect(Collectors.toList());
                }
            }
        }

        Specification<Resume> jobInSpec = filterSpecificationConverter
                .convert(filterBuilder.field("job").in(filterBuilder.input(arrJobIds)).get());

        Specification<Resume> finalSpec = jobInSpec.and(specification);

        PaginationResultDTO resumes = this.resumeService.findAll(finalSpec, pageable);
        return ResponseFactory.success(resumes, "Get all resumes successfully");
    }

    @PostMapping("/by-user")
    public ResponseEntity<RestResponse<PaginationResultDTO>> getResumeByUser(Pageable pageable) {
        PaginationResultDTO result = this.resumeService.findAll(null, pageable);
        return ResponseFactory.success(result, "Get resume by user successfully");
    }
}
