package vn.hoidanit.jobhunter.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import vn.hoidanit.jobhunter.domain.Permission;
import vn.hoidanit.jobhunter.domain.dto.PaginationResultDTO;

public interface PermissionService {
    Permission create(Permission permission);

    Permission findOne(long id);

    PaginationResultDTO findAll(Specification<Permission> spec, Pageable pageable);

    Permission update(Permission permission);

    void delete(long id);

}
