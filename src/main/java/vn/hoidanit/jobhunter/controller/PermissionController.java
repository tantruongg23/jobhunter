package vn.hoidanit.jobhunter.controller;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
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
import vn.hoidanit.jobhunter.domain.Permission;
import vn.hoidanit.jobhunter.domain.dto.PaginationResultDTO;
import vn.hoidanit.jobhunter.exception.IdInvalidException;
import vn.hoidanit.jobhunter.response.ResponseFactory;
import vn.hoidanit.jobhunter.response.RestResponse;
import vn.hoidanit.jobhunter.service.PermissionService;

@RestController
@RequestMapping("/api/v1/permissions")
public class PermissionController {

    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @PostMapping
    public ResponseEntity<RestResponse<Permission>> createPermission(@Valid @RequestBody Permission permission) {
        Permission createdPermission = this.permissionService.create(permission);
        return ResponseFactory.success(createdPermission, "Create permission successfully", HttpStatus.CREATED);
    }

    @PutMapping
    public ResponseEntity<RestResponse<Permission>> updatePermission(
            @Valid @RequestBody Permission permission) {

        Permission updatedPermission = this.permissionService.update(permission);
        return ResponseFactory.success(updatedPermission, "Update permission successfully");
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestResponse<Permission>> getPermission(@PathVariable(name = "id") long id) {
        Permission permission = this.permissionService.findOne(id);
        return ResponseFactory.success(permission, "Get permission successfully");
    }

    @GetMapping
    public ResponseEntity<RestResponse<PaginationResultDTO>> getAllPermissions(
            @Filter Specification<Permission> specification,
            Pageable pageable) {
        PaginationResultDTO result = this.permissionService.findAll(specification, pageable);
        return ResponseFactory.success(result, "Get all permissions successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RestResponse<Void>> deletePermission(@PathVariable(name = "id") long id) {
        this.permissionService.delete(id);
        return ResponseFactory.noContent("Delete permission successfully");
    }
}
