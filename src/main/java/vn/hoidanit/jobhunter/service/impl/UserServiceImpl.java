package vn.hoidanit.jobhunter.service.impl;

import java.util.LinkedList;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.hoidanit.jobhunter.domain.User;

import vn.hoidanit.jobhunter.domain.dto.PaginationResultDTO;
import vn.hoidanit.jobhunter.domain.dto.request.ReqUserCreateDTO;
import vn.hoidanit.jobhunter.domain.dto.response.ResUserDTO;
import vn.hoidanit.jobhunter.domain.dto.response.ResUserUpdateDTO;
import vn.hoidanit.jobhunter.exception.EmailExistedException;
import vn.hoidanit.jobhunter.exception.IdInvalidException;

import vn.hoidanit.jobhunter.repository.UserRepository;
import vn.hoidanit.jobhunter.service.UserService;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public ReqUserCreateDTO create(User user) throws EmailExistedException {
        if (this.userRepository.existsByEmail(user.getEmail())) {
            throw new EmailExistedException("Email " + user.getEmail() + " is existed. Please use another email!");
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        User createdUser = userRepository.save(user);

        ReqUserCreateDTO userDTO = ReqUserCreateDTO.builder()
                .id(createdUser.getId())
                .email(createdUser.getEmail())
                .gender(createdUser.getGender())
                .address(createdUser.getAddress())
                .age(createdUser.getAge())
                .createdAt(createdUser.getCreatedAt())
                .build();

        return userDTO;
    }

    @Override
    @Transactional(readOnly = true)
    public ResUserDTO findOne(long id) {
        User existedUser = this.findById(id);

        return this.convertToUserDTO(existedUser);
    }

    public User findById(long id) {
        User existedUser = userRepository.findById(id)
                .orElseThrow(() -> new IdInvalidException("User not found for ID " + id));

        return existedUser;
    }

    public ResUserDTO convertToUserDTO(User user) {
        ResUserDTO userDTO = ResUserDTO.builder()
                .id(user.getId())
                .name(user.getName())
                .gender(user.getGender())
                .address(user.getAddress())
                .age(user.getAge())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();

        return userDTO;

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

    @Override
    public ResUserUpdateDTO update(ResUserUpdateDTO user) {
        User existedUser = findById(user.getId()); // throws exception if not found

        existedUser.setName(user.getName());
        existedUser.setGender(user.getGender());
        existedUser.setAddress(user.getAddress());
        existedUser.setAge(user.getAge());

        existedUser = userRepository.save(existedUser);
        ResUserUpdateDTO userUpdateDTO = ResUserUpdateDTO.builder()
                .id(existedUser.getId())
                .name(existedUser.getName())
                .gender(existedUser.getGender())
                .address(existedUser.getAddress())
                .age(existedUser.getAge())
                .updatedAt(existedUser.getUpdatedAt())
                .build();

        return userUpdateDTO;
    }

    @Override
    public void delete(long id) {
        // Optional check if user exists, or just call deleteById
        if (!userRepository.existsById(id)) {
            throw new IdInvalidException("User does not found for ID " + id);
        }
        userRepository.deleteById(id);
    }

    @Override
    public User findByEmail(String email) {
        return this.userRepository.findByEmail(email);
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
