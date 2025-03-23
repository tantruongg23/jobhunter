package vn.hoidanit.jobhunter.service.impl;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.Permission;
import vn.hoidanit.jobhunter.domain.dto.PaginationResultDTO;
import vn.hoidanit.jobhunter.exception.IdInvalidException;
import vn.hoidanit.jobhunter.repository.PermissionRepository;
import vn.hoidanit.jobhunter.service.PermissionService;

@Service
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;

    public PermissionServiceImpl(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    @Override
    public Permission create(Permission permission) {
        boolean isExisted = this.permissionRepository.existsByApiPathAndMethodAndModule(permission.getApiPath(),
                permission.getMethod(), permission.getModule());
        if (isExisted) {
            throw new IdInvalidException("Permission has already existed");
        }
        Permission cPermission = this.permissionRepository.save(permission);
        return cPermission;
    }

    @Override
    public Permission update(Permission permission) {
        Permission curPermission = findById(permission.getId());

        boolean isExisted = this.permissionRepository.existsByApiPathAndMethodAndModule(permission.getApiPath(),
                permission.getMethod(), permission.getModule());

        if (isExisted) {
            throw new IdInvalidException("Permission has already existed");
        }

        curPermission.setName(permission.getName());
        curPermission.setApiPath(permission.getApiPath());
        curPermission.setMethod(permission.getMethod());
        curPermission.setModule(permission.getModule());

        curPermission = this.permissionRepository.save(curPermission);
        return curPermission;
    }

    @Override
    public Permission findOne(long id) {
        return this.findById(id);
    }

    @Override
    public PaginationResultDTO findAll(Specification<Permission> spec, Pageable pageable) {
        Page<Permission> pagePermissions = this.permissionRepository.findAll(spec, pageable);

        PaginationResultDTO result = new PaginationResultDTO();
        PaginationResultDTO.Meta meta = new PaginationResultDTO.Meta();
        meta.setPage(pagePermissions.getNumber() + 1);
        meta.setPageSize(pagePermissions.getSize());
        meta.setPages(pagePermissions.getTotalPages());
        meta.setTotal(pagePermissions.getTotalElements());

        result.setMeta(meta);
        List<Permission> permissions = pagePermissions.getContent();
        result.setResult(permissions);

        return result;
    }

    @Override
    public void delete(long id) {
        Permission curPermission = this.findById(id);

        // delete permission_role
        curPermission.getRoles().forEach(role -> role.getPermissions().remove(curPermission));

        // delete permission
        this.permissionRepository.delete(curPermission);
    }

    public Permission findById(long id) {
        return this.permissionRepository.findById(id).orElseThrow(
                () -> new IdInvalidException("Permission not found for ID = " + id));
    }

}
