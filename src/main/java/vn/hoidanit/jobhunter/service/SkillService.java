package vn.hoidanit.jobhunter.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.dto.PaginationResultDTO;

public interface SkillService {
    Skill create(Skill skill);

    Skill findOne(long id);

    PaginationResultDTO findAll(Specification<Skill> spec, Pageable pageable);

    Skill update(Skill skill);

    void delete(long id);

}
