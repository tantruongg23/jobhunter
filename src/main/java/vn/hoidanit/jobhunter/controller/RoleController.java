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
import vn.hoidanit.jobhunter.domain.Role;
import vn.hoidanit.jobhunter.domain.dto.PaginationResultDTO;
import vn.hoidanit.jobhunter.response.ResponseFactory;
import vn.hoidanit.jobhunter.response.RestResponse;
import vn.hoidanit.jobhunter.service.RoleService;

@RestController
@RequestMapping("/api/v1/roles")
public class RoleController {

    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping
    public ResponseEntity<RestResponse<Role>> createRole(@Valid @RequestBody Role role) {
        Role createdRole = this.roleService.create(role);
        return ResponseFactory.success(createdRole, "Create role successfully", HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<RestResponse<Role>> updateRole(@Valid @RequestBody Role role) {
        Role updatedRole = this.roleService.update(role);
        return ResponseFactory.success(updatedRole, "Update role successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestResponse<Role>> getRole(@PathVariable(name = "id") long id) {
        Role currRole = this.roleService.findOne(id);
        return ResponseFactory.success(currRole, "Get role successfully");
    }

    @GetMapping
    public ResponseEntity<RestResponse<PaginationResultDTO>> getAllRoles(@Filter Specification<Role> specification,
            Pageable pageable) {
        PaginationResultDTO result = this.roleService.findAll(specification, pageable);
        return ResponseFactory.success(result, "Get all roles successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RestResponse<Void>> deleteRole(@PathVariable(name = "id") long id) {
        this.roleService.delete(id);
        return ResponseFactory.noContent("Delete role successfully");
    }
}
