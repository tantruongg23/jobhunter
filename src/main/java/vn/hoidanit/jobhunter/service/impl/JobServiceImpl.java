package vn.hoidanit.jobhunter.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.dto.PaginationResultDTO;
import vn.hoidanit.jobhunter.domain.dto.response.ResJobCreateDTO;
import vn.hoidanit.jobhunter.domain.dto.response.ResJobUpdateDTO;
import vn.hoidanit.jobhunter.exception.IdInvalidException;
import vn.hoidanit.jobhunter.repository.JobRepository;
import vn.hoidanit.jobhunter.repository.SkillRepository;
import vn.hoidanit.jobhunter.service.JobService;

@Service
public class JobServiceImpl implements JobService {
    private final JobRepository jobRepository;
    private final SkillRepository skillRepository;

    public JobServiceImpl(JobRepository jobRepository,
            SkillRepository skillRepository) {
        this.jobRepository = jobRepository;
        this.skillRepository = skillRepository;
    }

    @Override
    public ResJobCreateDTO create(Job job) {
        // Check skills in job
        List<Long> ids = new ArrayList<>();
        for (Skill skill : job.getSkills()) {
            if (skill.getId() != 0) {
                ids.add(skill.getId());
            }
        }
        List<Skill> skills = this.skillRepository.findByIdIn(ids);

        job.setSkills(skills);

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
        List<Long> ids = new ArrayList<>();
        for (Skill skill : job.getSkills()) {
            if (skill.getId() != 0) {
                ids.add(skill.getId());
            }
        }

        List<Skill> skills = this.skillRepository.findByIdIn(ids);

        job.setSkills(skills);

        createdJob = this.jobRepository.save(job);

        ResJobUpdateDTO resJobUpdateDTO = new ResJobUpdateDTO();
        if (createdJob != null) {
            resJobUpdateDTO.setId(createdJob.getId());
            resJobUpdateDTO.setName(createdJob.getName());
            resJobUpdateDTO.setDescription(createdJob.getDescription());
            resJobUpdateDTO.setLocation(createdJob.getLocation());
            resJobUpdateDTO.setSalary(createdJob.getSalary());
            resJobUpdateDTO.setQuantity(createdJob.getQuantity());
            resJobUpdateDTO.setLevel(createdJob.getLevel());
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
