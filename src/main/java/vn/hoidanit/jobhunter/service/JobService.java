package vn.hoidanit.jobhunter.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import vn.hoidanit.jobhunter.domain.Job;
import vn.hoidanit.jobhunter.domain.dto.PaginationResultDTO;
import vn.hoidanit.jobhunter.domain.dto.response.ResJobCreateDTO;
import vn.hoidanit.jobhunter.domain.dto.response.ResJobUpdateDTO;

public interface JobService {
    ResJobCreateDTO create(Job skill);

    Job findOne(long id);

    PaginationResultDTO findAll(Specification<Job> spec, Pageable pageable);

    ResJobUpdateDTO update(Job skill);

    void delete(long id);
}
