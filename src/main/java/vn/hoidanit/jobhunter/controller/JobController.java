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
import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.dto.PaginationResultDTO;
import vn.hoidanit.jobhunter.domain.dto.response.ResJobCreateDTO;
import vn.hoidanit.jobhunter.domain.dto.response.ResJobUpdateDTO;
import vn.hoidanit.jobhunter.response.ResponseFactory;
import vn.hoidanit.jobhunter.response.RestResponse;
import vn.hoidanit.jobhunter.service.JobService;

@RestController
@RequestMapping("/api/v1/jobs")
public class JobController {

    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping
    public ResponseEntity<RestResponse<ResJobCreateDTO>> createJob(@Valid @RequestBody Job job) {
        ResJobCreateDTO createdJob = this.jobService.create(job);
        return ResponseFactory.success(createdJob, "Create job successfully", HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<RestResponse<ResJobUpdateDTO>> updateJob(@Valid @RequestBody Job job) {
        ResJobUpdateDTO updatedJob = this.jobService.update(job);
        return ResponseFactory.success(updatedJob, "Update job successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestResponse<Job>> getJob(@PathVariable(name = "id") long id) {
        return ResponseFactory.success(this.jobService.findOne(id), "Get job successfully");
    }

    @GetMapping
    public ResponseEntity<RestResponse<PaginationResultDTO>> getAllJobs(
            @Filter Specification<Job> specification,
            Pageable pageable) {
        PaginationResultDTO jobs = this.jobService.findAll(specification, pageable);
        return ResponseFactory.success(jobs, "Get all jobs successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RestResponse<Void>> deleteJob(@PathVariable(name = "id") long id) {
        this.jobService.delete(id);
        return ResponseFactory.noContent("Delete job successfully");
    }
}
