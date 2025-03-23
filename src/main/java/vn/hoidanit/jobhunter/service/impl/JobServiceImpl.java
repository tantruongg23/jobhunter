package vn.hoidanit.jobhunter.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.dto.PaginationResultDTO;
import vn.hoidanit.jobhunter.domain.dto.response.ResJobCreateDTO;
import vn.hoidanit.jobhunter.domain.dto.response.ResJobUpdateDTO;
import vn.hoidanit.jobhunter.exception.IdInvalidException;
import vn.hoidanit.jobhunter.repository.CompanyRepository;
import vn.hoidanit.jobhunter.repository.JobRepository;
import vn.hoidanit.jobhunter.repository.SkillRepository;
import vn.hoidanit.jobhunter.service.JobService;

@Service
public class JobServiceImpl implements JobService {
    private final JobRepository jobRepository;
    private final SkillRepository skillRepository;
    private final CompanyRepository companyRepository;

    public JobServiceImpl(JobRepository jobRepository,
            SkillRepository skillRepository,
            CompanyRepository companyRepository) {
        this.jobRepository = jobRepository;
        this.skillRepository = skillRepository;
        this.companyRepository = companyRepository;
    }

    @Override
    public ResJobCreateDTO create(Job job) {
        // Check skills in job
        if (job.getSkills() != null) {
            List<Long> ids = new ArrayList<>();
            for (Skill skill : job.getSkills()) {
                if (skill.getId() != 0) {
                    ids.add(skill.getId());
                }
            }
            List<Skill> skills = this.skillRepository.findByIdIn(ids);

            job.setSkills(skills);
        }

        // Check company
        if (job.getCompany() != null) {
            Company company = this.companyRepository.findById(job.getCompany().getId()).orElse(null);
            if (company != null) {
                job.setCompany(company);
            }
        }

        Job createdJob = this.jobRepository.save(job);

        ResJobCreateDTO resJobCreateDTO = new ResJobCreateDTO();
        if (createdJob != null) {
            resJobCreateDTO.setId(createdJob.getId());
            resJobCreateDTO.setName(createdJob.getName());
            resJobCreateDTO.setDescription(createdJob.getDescription());
            resJobCreateDTO.setLocation(createdJob.getLocation());
            resJobCreateDTO.setSalary(createdJob.getSalary());
            resJobCreateDTO.setQuantity(createdJob.getQuantity());
            resJobCreateDTO.setLevel(createdJob.getLevel());
            resJobCreateDTO.setActive(createdJob.isActive());
            resJobCreateDTO.setStartDate(createdJob.getStartDate());
            resJobCreateDTO.setEndDate(createdJob.getEndDate());
            resJobCreateDTO.setCreatedAt(createdJob.getCreatedAt());
            resJobCreateDTO.setCreatedBy(createdJob.getCreatedBy());

            List<String> strSkills = new ArrayList<>();
            for (Skill skill : createdJob.getSkills()) {
                strSkills.add(skill.getName());
            }
            resJobCreateDTO.setSkills(strSkills);
        }
        return resJobCreateDTO;
    }

    @Override
    public ResJobUpdateDTO update(Job job) {
        Job createdJob = findById(job.getId());

        // Check skills in job
        if (job.getSkills() != null) {
            List<Long> ids = new ArrayList<>();
            for (Skill skill : job.getSkills()) {
                if (skill.getId() != 0) {
                    ids.add(skill.getId());
                }
            }

            List<Skill> skills = this.skillRepository.findByIdIn(ids);

            createdJob.setSkills(skills);
        }

        // Check company
        if (job.getCompany() != null) {
            Company company = this.companyRepository.findById(job.getCompany().getId()).orElse(null);
            if (company != null) {
                createdJob.setCompany(company);
            }
        }

        // Update info
        createdJob.setName(job.getName());
        createdJob.setDescription(job.getDescription());
        createdJob.setLocation(job.getLocation());
        createdJob.setSalary(job.getSalary());
        createdJob.setQuantity(job.getQuantity());
        createdJob.setLevel(job.getLevel());
        createdJob.setActive(job.isActive());
        createdJob.setStartDate(job.getStartDate());
        createdJob.setEndDate(job.getEndDate());

        createdJob = this.jobRepository.save(createdJob);

        ResJobUpdateDTO resJobUpdateDTO = new ResJobUpdateDTO();
        if (createdJob != null) {
            resJobUpdateDTO.setId(createdJob.getId());
            resJobUpdateDTO.setName(createdJob.getName());
            resJobUpdateDTO.setDescription(createdJob.getDescription());
            resJobUpdateDTO.setLocation(createdJob.getLocation());
            resJobUpdateDTO.setSalary(createdJob.getSalary());
            resJobUpdateDTO.setQuantity(createdJob.getQuantity());
            resJobUpdateDTO.setLevel(createdJob.getLevel());
            resJobUpdateDTO.setActive(createdJob.isActive());
            resJobUpdateDTO.setStartDate(createdJob.getStartDate());
            resJobUpdateDTO.setEndDate(createdJob.getEndDate());
            resJobUpdateDTO.setUpdatedAt(createdJob.getUpdatedAt());
            resJobUpdateDTO.setUpdatedBy(createdJob.getUpdatedBy());

            List<String> strSkills = new ArrayList<>();
            for (Skill skill : createdJob.getSkills()) {
                strSkills.add(skill.getName());
            }
            resJobUpdateDTO.setSkills(strSkills);
        }
        return resJobUpdateDTO;

    }

    @Override
    public Job findOne(long id) {
        return this.findById(id);
    }

    @Override
    public PaginationResultDTO findAll(Specification<Job> spec, Pageable pageable) {
        Page<Job> pageJobs = this.jobRepository.findAll(spec, pageable); // PaginationResultDTO result = new

        PaginationResultDTO result = new PaginationResultDTO();
        PaginationResultDTO.Meta meta = new PaginationResultDTO.Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(pageJobs.getTotalPages());
        meta.setTotal(pageJobs.getTotalElements());

        result.setMeta(meta);

        List<Job> jobs = pageJobs.getContent();
        result.setResult(jobs);

        return result;
    }

    @Override
    public void delete(long id) {
        this.findById(id);

        this.jobRepository.deleteById(id);
        return;
    }

    public Job findById(long id) {
        return this.jobRepository.findById(id)
                .orElseThrow(() -> new IdInvalidException("Job not found for ID = " + id));
    }
}
