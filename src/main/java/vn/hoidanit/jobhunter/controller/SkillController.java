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
import vn.hoidanit.jobhunter.domain.Skill;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.dto.PaginationResultDTO;
import vn.hoidanit.jobhunter.response.ResponseFactory;
import vn.hoidanit.jobhunter.response.RestResponse;
import vn.hoidanit.jobhunter.service.SkillService;

@RestController
@RequestMapping("/api/v1/skills")
public class SkillController {

    private final SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @PostMapping
    public ResponseEntity<RestResponse<Skill>> createSkill(@Valid @RequestBody Skill skill) {
        return ResponseFactory.success(this.skillService.create(skill), "Create skill successfully",
                HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<RestResponse<Skill>> updateSkill(@Valid @RequestBody Skill skill) {
        return ResponseFactory.success(this.skillService.update(skill), "Update skill successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestResponse<Skill>> getSkill(@PathVariable(name = "id") long id) {
        return ResponseFactory.success(this.skillService.findOne(id), "Fetch skill successfully");
    }

    @GetMapping
    public ResponseEntity<RestResponse<PaginationResultDTO>> getAllSkills(@Filter Specification<Skill> specification,
            Pageable pageable) {
        PaginationResultDTO skills = this.skillService.findAll(specification, pageable);
        return ResponseFactory.success(skills, "Fetch all skills successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RestResponse<Void>> deleteSkill(@PathVariable(name = "id") long id) {
        this.skillService.delete(id);
        return ResponseFactory.noContent("Delete skill successfully");
    }
}
