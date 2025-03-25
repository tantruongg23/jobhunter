package vn.hoidanit.jobhunter.service.impl;

import java.util.LinkedList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.hoidanit.jobhunter.domain.Company;
import vn.hoidanit.jobhunter.domain.Role;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.dto.PaginationResultDTO;
import vn.hoidanit.jobhunter.domain.dto.response.ResCompanyUserDTO;
import vn.hoidanit.jobhunter.domain.dto.response.ResUserCreateDTO;
import vn.hoidanit.jobhunter.domain.dto.response.ResUserDTO;
import vn.hoidanit.jobhunter.domain.dto.response.ResUserUpdateDTO;
import vn.hoidanit.jobhunter.domain.dto.response.ResUserDTO.ResRoleUserDTO;
import vn.hoidanit.jobhunter.exception.EmailExistedException;
import vn.hoidanit.jobhunter.exception.IdInvalidException;
import vn.hoidanit.jobhunter.repository.UserRepository;
import vn.hoidanit.jobhunter.service.CompanyService;
import vn.hoidanit.jobhunter.service.RoleService;
import vn.hoidanit.jobhunter.service.UserService;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final CompanyService companyService;
    private final RoleService roleService;

    public UserServiceImpl(UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            CompanyService companyService,
            RoleService roleService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.companyService = companyService;
        this.roleService = roleService;
    }

    @Override
    public ResUserCreateDTO create(User user) throws EmailExistedException {
        if (this.userRepository.existsByEmail(user.getEmail())) {
            throw new EmailExistedException("Email " + user.getEmail() + " is existed. Please use another email!");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if (user.getCompany() != null) {
            Company company = this.companyService.findOne(user.getCompany().getId());
            if (company != null) {
                user.setCompany(company);
            } else {
                user.setCompany(null);
            }
        }

        // check role
        if (user.getRole() != null) {
            Role role = this.roleService.findOne(user.getRole().getId());
            user.setRole(role != null ? role : null);
        }

        User createdUser = userRepository.save(user);

        ResCompanyUserDTO companyUserDTO = null;
        if (createdUser.getCompany() != null) {
            companyUserDTO = new ResCompanyUserDTO();
            companyUserDTO.setId(createdUser.getCompany().getId());
            companyUserDTO.setName(createdUser.getCompany().getName());
        }

        ResRoleUserDTO roleUserDTO = null;
        if (createdUser.getRole() != null) {
            roleUserDTO = new ResRoleUserDTO();
            roleUserDTO.setId(createdUser.getRole().getId());
            roleUserDTO.setName(createdUser.getRole().getName());
        }

        ResUserCreateDTO userDTO = ResUserCreateDTO.builder()
                .id(createdUser.getId())
                .email(createdUser.getEmail())
                .gender(createdUser.getGender())
                .address(createdUser.getAddress())
                .age(createdUser.getAge())
                .createdAt(createdUser.getCreatedAt())
                .company(companyUserDTO)
                .role(roleUserDTO)
                .build();

        return userDTO;
    }

    @Override
    public ResUserUpdateDTO update(ResUserUpdateDTO user) {
        User existedUser = findById(user.getId()); // throws exception if not found

        if (user.getCompany() != null) {
            Company company = this.companyService.findOne(user.getCompany().getId());
            if (company != null) {
                existedUser.setCompany(company);
            } else {
                existedUser.setCompany(null);
            }
        }

        // check role
        if (user.getRole() != null) {
            Role role = this.roleService.findOne(user.getRole().getId());
            existedUser.setRole(role != null ? role : null);
        }

        existedUser.setName(user.getName());
        existedUser.setGender(user.getGender());
        existedUser.setAddress(user.getAddress());
        existedUser.setAge(user.getAge());
        existedUser = userRepository.save(existedUser);

        ResCompanyUserDTO companyUserDTO = null;
        if (existedUser.getCompany() != null) {
            companyUserDTO = new ResCompanyUserDTO();
            companyUserDTO.setId(existedUser.getCompany().getId());
            companyUserDTO.setName(existedUser.getCompany().getName());
        }

        ResRoleUserDTO roleUserDTO = null;
        if (existedUser.getRole() != null) {
            roleUserDTO = new ResRoleUserDTO();
            roleUserDTO.setId(existedUser.getRole().getId());
            roleUserDTO.setName(existedUser.getRole().getName());
        }

        ResUserUpdateDTO userUpdateDTO = ResUserUpdateDTO.builder()
                .id(existedUser.getId())
                .name(existedUser.getName())
                .gender(existedUser.getGender())
                .address(existedUser.getAddress())
                .age(existedUser.getAge())
                .updatedAt(existedUser.getUpdatedAt())
                .company(companyUserDTO)
                .role(roleUserDTO)
                .build();

        return userUpdateDTO;
    }

    @Override
    @Transactional(readOnly = true)
    public ResUserDTO findOne(long id) {
        User existedUser = this.findById(id);

        return this.convertToUserDTO(existedUser);
    }

    @Override
    public PaginationResultDTO findAll(Specification<User> spec, Pageable pageable) {
        Page<User> pageUsers = this.userRepository.findAll(spec, pageable);
        PaginationResultDTO result = new PaginationResultDTO();
        PaginationResultDTO.Meta meta = new PaginationResultDTO.Meta();
        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(pageUsers.getTotalPages());
        meta.setTotal(pageUsers.getTotalElements());

        result.setMeta(meta);

        List<User> users = pageUsers.getContent();
        List<ResUserDTO> userDTOs = new LinkedList<>();
        for (User user : users) {
            userDTOs.add(this.convertToUserDTO(user));
        }
        result.setResult(userDTOs);

        return result;
    }

    public ResUserDTO convertToUserDTO(User user) {

        ResCompanyUserDTO companyUserDTO = null;
        if (user.getCompany() != null) {
            companyUserDTO = new ResCompanyUserDTO();
            companyUserDTO.setId(user.getCompany().getId());
            companyUserDTO.setName(user.getCompany().getName());
        }

        ResRoleUserDTO roleUserDTO = null;
        if (user.getRole() != null) {
            roleUserDTO = new ResRoleUserDTO();
            roleUserDTO.setId(user.getRole().getId());
            roleUserDTO.setName(user.getRole().getName());
        }

        ResUserDTO userDTO = ResUserDTO.builder()
                .id(user.getId())
                .email(user.getEmail())
                .name(user.getName())
                .gender(user.getGender())
                .address(user.getAddress())
                .age(user.getAge())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .company(companyUserDTO)
                .role(roleUserDTO)
                .build();

        return userDTO;
    }

    @Override
    public void delete(long id) {
        // Optional check if user exists, or just call deleteById
        if (!userRepository.existsById(id)) {
            throw new IdInvalidException("User does not found for ID " + id);
        }
        userRepository.deleteById(id);
    }

    public User findById(long id) {
        User existedUser = userRepository.findById(id)
                .orElseThrow(() -> new IdInvalidException("User not found for ID " + id));

        return existedUser;
    }

    @Override
    public User findByEmail(String email) {
        return this.userRepository.findByEmail(email);
    }

    @Override
    public boolean isEmailExist(String email) {
        return this.userRepository.existsByEmail(email);
    }

    public void updateUserToken(String token, String email) {
        User currentUser = this.findByEmail(email);
        if (currentUser != null) {
            currentUser.setRefreshToken(token);
            this.userRepository.save(currentUser);
        }
    }

    @Override
    public User getUserByRefreshTokenAndEmail(String refreshToken, String email) {
        return this.userRepository.findByRefreshTokenAndEmail(refreshToken, email);
    }

}
