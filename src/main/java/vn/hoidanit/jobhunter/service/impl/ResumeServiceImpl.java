package vn.hoidanit.jobhunter.service.impl;

import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.Resume;
import vn.hoidanit.jobhunter.domain.Resume;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.dto.PaginationResultDTO;
import vn.hoidanit.jobhunter.domain.dto.response.ResResumeCreateDTO;
import vn.hoidanit.jobhunter.domain.dto.response.ResResumeDTO;
import vn.hoidanit.jobhunter.domain.dto.response.ResResumeUpdateDTO;
import vn.hoidanit.jobhunter.exception.IdInvalidException;
import vn.hoidanit.jobhunter.repository.JobRepository;
import vn.hoidanit.jobhunter.repository.ResumeRepository;
import vn.hoidanit.jobhunter.repository.UserRepository;
import vn.hoidanit.jobhunter.service.ResumeService;

@Service
public class ResumeServiceImpl implements ResumeService {
    private final ResumeRepository resumeRepository;
    private final UserRepository userRepository;
    private final JobRepository jobRepository;

    public ResumeServiceImpl(ResumeRepository resumeRepository,
            UserRepository userRepository,
            JobRepository jobRepository) {
        this.resumeRepository = resumeRepository;
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
    }

    @Override
    public ResResumeCreateDTO create(Resume resume) {

        if (resume.getUser() == null) {
            throw new IdInvalidException("User not found for ID = " + resume.getUser());
        }

        if (resume.getJob() == null) {
            throw new IdInvalidException("Job not found for ID = " + resume.getJob());
        }

        User currentUser = this.userRepository
                .findById(resume.getUser().getId())
                .orElseThrow(
                        () -> new IdInvalidException("User not found for ID = " + resume.getUser().getId()));

        Job currentJob = this.jobRepository.findById(resume.getJob().getId()).orElseThrow(
                () -> new IdInvalidException("Job not found for ID = " + resume.getJob().getId()));

        resume.setUser(currentUser);
        resume.setJob(currentJob);

        Resume createdResume = this.resumeRepository.save(resume);
        ResResumeCreateDTO res = new ResResumeCreateDTO();

        if (createdResume != null) {
            res.setId(createdResume.getId());
            res.setCreatedAt(createdResume.getCreatedAt());
            res.setCreatedBy(createdResume.getCreatedBy());
        }
        return res;
    }

    @Override
    public ResResumeUpdateDTO update(Resume resume) {
        Resume currentResume = this.findById(resume.getId());

        if (resume.getStatus() != null) {
            currentResume.setStatus(resume.getStatus());
        }

        currentResume = this.resumeRepository.save(currentResume);

        ResResumeUpdateDTO resResumeUpdateDTO = new ResResumeUpdateDTO();
        resResumeUpdateDTO.setId(currentResume.getId());
        resResumeUpdateDTO.setUpdatedAt(currentResume.getUpdatedAt());
        resResumeUpdateDTO.setUpdatedBy(currentResume.getUpdatedBy());

        return resResumeUpdateDTO;
    }

    @Override
    public void delete(long id) {
        this.findById(id);

        this.resumeRepository.deleteById(id);
    }

    @Override
    public ResResumeDTO findOne(long id) {
        Resume currentResume = this.findById(id);

        return this.convertToResumeDTO(currentResume);
    }

    @Override
    public PaginationResultDTO findAll(Specification spec, Pageable pageable) {

        Page<Resume> pageResumes = this.resumeRepository.findAll(spec, pageable); // PaginationResultDTO result = new

        PaginationResultDTO result = new PaginationResultDTO();
        PaginationResultDTO.Meta meta = new PaginationResultDTO.Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(pageResumes.getTotalPages());
        meta.setTotal(pageResumes.getTotalElements());

        result.setMeta(meta);

        List<ResResumeDTO> resumes = pageResumes.getContent().stream()
                .map(resume -> this.convertToResumeDTO(resume))
                .collect(Collectors.toList());

        result.setResult(resumes);

        return result;
    }

    public ResResumeDTO convertToResumeDTO(Resume currentResume) {
        ResResumeDTO resResumeDTO = new ResResumeDTO();

        resResumeDTO.setId(currentResume.getId());
        resResumeDTO.setEmail(currentResume.getEmail());
        resResumeDTO.setUrl(currentResume.getUrl());
        resResumeDTO.setStatus(currentResume.getStatus());
        resResumeDTO.setCreatedAt(currentResume.getCreatedAt());
        resResumeDTO.setCreatedBy(currentResume.getCreatedBy());
        resResumeDTO.setUpdatedAt(currentResume.getUpdatedAt());
        resResumeDTO.setUpdatedBy(currentResume.getUpdatedBy());

        if (currentResume.getJob() != null && currentResume.getJob().getCompany() != null) {
            resResumeDTO.setCompanyName(currentResume.getJob().getCompany().getName());
        }

        ResResumeDTO.ResumeUserDTO resumeUserDTO = new ResResumeDTO.ResumeUserDTO();
        resumeUserDTO.setId(currentResume.getUser().getId());
        resumeUserDTO.setName(currentResume.getUser().getName());
        resResumeDTO.setUser(resumeUserDTO);

        ResResumeDTO.ResumeJobDTO resumeJobDTO = new ResResumeDTO.ResumeJobDTO();
        resumeJobDTO.setId(currentResume.getJob().getId());
        resumeJobDTO.setName(currentResume.getJob().getName());
        resResumeDTO.setJob(resumeJobDTO);

        return resResumeDTO;
    }

    public Resume findById(long id) {
        return this.resumeRepository.findById(id).orElseThrow(
                () -> new IdInvalidException("Resume not found for ID = " + id));
    }
}
