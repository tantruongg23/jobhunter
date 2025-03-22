package vn.hoidanit.jobhunter.controller;

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

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.Resume;
import vn.hoidanit.jobhunter.domain.dto.PaginationResultDTO;
import vn.hoidanit.jobhunter.domain.dto.response.ResResumeCreateDTO;
import vn.hoidanit.jobhunter.domain.dto.response.ResResumeDTO;
import vn.hoidanit.jobhunter.domain.dto.response.ResResumeUpdateDTO;
import vn.hoidanit.jobhunter.response.ResponseFactory;
import vn.hoidanit.jobhunter.response.RestResponse;
import vn.hoidanit.jobhunter.service.ResumeService;

@RestController
@RequestMapping("/api/v1/resumes")
public class ResumeController {
    private final ResumeService resumeService;

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
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
    public ResponseEntity<RestResponse<PaginationResultDTO>> getAllResumes(@Filter Specification specification,
            Pageable pageable) {
        PaginationResultDTO resumes = this.resumeService.findAll(specification, pageable);
        return ResponseFactory.success(resumes, "Get all resumes successfully");
    }
}
