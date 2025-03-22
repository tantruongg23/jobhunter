package vn.hoidanit.jobhunter.service.impl;

import java.util.LinkedList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.dto.PaginationResultDTO;
import vn.hoidanit.jobhunter.domain.dto.response.ResUserDTO;
import vn.hoidanit.jobhunter.exception.IdInvalidException;
import vn.hoidanit.jobhunter.repository.SkillRepository;
import vn.hoidanit.jobhunter.service.SkillService;

@Service
public class SkillServiceImpl implements SkillService {

    private final SkillRepository skillRepository;

    public SkillServiceImpl(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    @Override
    public Skill create(Skill skill) {

        if (this.skillRepository.existsByName(skill.getName())) {
            throw new IdInvalidException("Skill is existed for name = " + skill.getName());
        }
        return this.skillRepository.save(skill);
    }

    @Override
    public Skill update(Skill skill) {
        Skill existedSkill = this.findById(skill.getId());

        existedSkill.setName(skill.getName());
        return this.skillRepository.save(existedSkill);
    }

    @Override
    public Skill findOne(long id) {
        return this.findById(id);
    }

    @Override
    public PaginationResultDTO findAll(Specification<Skill> spec, Pageable pageable) {
        Page<Skill> pageSkills = this.skillRepository.findAll(spec, pageable); // PaginationResultDTO result = new

        PaginationResultDTO result = new PaginationResultDTO();
        PaginationResultDTO.Meta meta = new PaginationResultDTO.Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(pageSkills.getTotalPages());
        meta.setTotal(pageSkills.getTotalElements());

        result.setMeta(meta);

        List<Skill> skills = pageSkills.getContent();
        result.setResult(skills);

        return result;
    }

    @Override
    public void delete(long id) {
        Skill currentSkill = this.findById(id);

        // Remove job-skill record in job_skill table
        currentSkill.getJobs().forEach(job -> job.getSkills().remove(currentSkill));

        this.skillRepository.deleteById(id);
    }

    public Skill findById(long id) {
        Skill existedSkill = this.skillRepository.findById(id)
                .orElseThrow(() -> new IdInvalidException("Skill not found for ID " + id));

        return existedSkill;
    }

    public Skill findByName(String name) {
        Skill existedSkill = this.skillRepository.findByName(name)
                .orElse(null);

        return existedSkill;
    }
}
