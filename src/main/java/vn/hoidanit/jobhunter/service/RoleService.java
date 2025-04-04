package vn.hoidanit.jobhunter.service;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import vn.hoidanit.jobhunter.domain.Role;
import vn.hoidanit.jobhunter.domain.dto.PaginationResultDTO;

public interface RoleService {
    Role create(Role role);

    Role findOne(long id);

    PaginationResultDTO findAll(Specification<Role> spec, Pageable pageable);

    Role update(Role role);

    void delete(long id);
}
