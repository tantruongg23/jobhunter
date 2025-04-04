package vn.hoidanit.jobhunter.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Permission;
import vn.hoidanit.jobhunter.domain.Role;
import vn.hoidanit.jobhunter.domain.dto.PaginationResultDTO;
import vn.hoidanit.jobhunter.exception.IdInvalidException;
import vn.hoidanit.jobhunter.repository.PermissionRepository;
import vn.hoidanit.jobhunter.repository.RoleRepository;
import vn.hoidanit.jobhunter.service.RoleService;

@Service
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public RoleServiceImpl(RoleRepository roleRepository,
            PermissionRepository permissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    @Override
    public Role create(Role role) {
        boolean isExisted = this.roleRepository.existsByName(role.getName());
        if (isExisted) {
            throw new IdInvalidException("Role with name = " + role.getName() + " is existed");
        }

        // check permission
        if (role.getPermissions() != null) {
            List<Long> ids = new ArrayList<>();
            for (Permission p : role.getPermissions()) {
                ids.add(p.getId());
            }

            List<Permission> permissions = this.permissionRepository.findByIdIn(ids);

            if (permissions != null) {
                role.setPermissions(permissions);
            }
        }

        return this.roleRepository.save(role);
    }

    @Override
    public Role update(Role role) {
        Role currRole = this.findById(role.getId());

        // boolean isExisted = this.roleRepository.existsByName(role.getName());
        // if (!isExisted) {
        // throw new IdInvalidException("Role with name = " + role.getName() + " is not
        // existed");
        // }

        // check permission
        if (role.getPermissions() != null) {
            List<Long> ids = new ArrayList<>();
            for (Permission p : role.getPermissions()) {
                ids.add(p.getId());
            }

            List<Permission> permissions = this.permissionRepository.findByIdIn(ids);

            if (permissions != null) {
                role.setPermissions(permissions);
            }
        }

        currRole.setName(role.getName());
        currRole.setDescription(role.getDescription());
        currRole.setActive(role.isActive());
        currRole.setPermissions(role.getPermissions());

        return this.roleRepository.save(currRole);
    }

    @Override
    public Role findOne(long id) {
        return this.findById(id);
    }

    @Override
    public PaginationResultDTO findAll(Specification<Role> spec, Pageable pageable) {
        Page<Role> pageRoles = this.roleRepository.findAll(spec, pageable);
        PaginationResultDTO result = new PaginationResultDTO();
        PaginationResultDTO.Meta meta = new PaginationResultDTO.Meta();

        meta.setPage(pageRoles.getNumber() + 1);
        meta.setPageSize(pageRoles.getSize());
        meta.setPages(pageRoles.getTotalPages());
        meta.setTotal(pageRoles.getTotalElements());

        result.setMeta(meta);
        List<Role> roles = pageRoles.getContent();
        result.setResult(roles);

        return result;
    }

    @Override
    public void delete(long id) {
        Role curRole = findById(id);
        this.roleRepository.delete(curRole);
    }

    public Role findById(long id) {
        return this.roleRepository.findById(id).orElseThrow(
                () -> new IdInvalidException("Id not found for ID = " + id));
    }
}
