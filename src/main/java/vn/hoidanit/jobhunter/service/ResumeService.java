package vn.hoidanit.jobhunter.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import vn.hoidanit.jobhunter.domain.Resume;
import vn.hoidanit.jobhunter.domain.dto.PaginationResultDTO;
import vn.hoidanit.jobhunter.domain.dto.response.ResResumeCreateDTO;
import vn.hoidanit.jobhunter.domain.dto.response.ResResumeDTO;
import vn.hoidanit.jobhunter.domain.dto.response.ResResumeUpdateDTO;

public interface ResumeService {
    ResResumeCreateDTO create(Resume resume);

    ResResumeDTO findOne(long id);

    PaginationResultDTO findAll(Specification<Resume> spec, Pageable pageable);

    ResResumeUpdateDTO update(Resume resume);

    void delete(long id);

    PaginationResultDTO findByUser(Pageable pageable);

}