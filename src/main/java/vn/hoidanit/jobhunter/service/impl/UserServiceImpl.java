package vn.hoidanit.jobhunter.service.impl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.dto.Meta;
import vn.hoidanit.jobhunter.domain.dto.PaginationResultDTO;
import vn.hoidanit.jobhunter.exception.UserNotFoundException;
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
    public User create(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public User findOne(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found for ID " + id));
    }

    @Override
    public PaginationResultDTO findAll(Pageable pageable) {
        Page<User> pageUsers = this.userRepository.findAll(pageable);
        PaginationResultDTO result = new PaginationResultDTO();
        Meta meta = new Meta();
        meta.setPage(pageUsers.getNumber() + 1);
        meta.setPageSize(pageUsers.getSize());
        meta.setPages(pageUsers.getTotalPages());
        meta.setTotal(pageUsers.getTotalElements());

        result.setMeta(meta);
        result.setResult(pageUsers.getContent());

        return result;
    }

    @Override
    public User update(User updatedUser) {
        User existing = findOne(updatedUser.getId()); // throws exception if not found
        existing.setName(updatedUser.getName());
        existing.setEmail(updatedUser.getEmail());
        existing.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        return userRepository.save(existing);
    }

    @Override
    public void delete(long id) {
        // Optional check if user exists, or just call deleteById
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User does not exist for " + id);
        }
        userRepository.deleteById(id);
    }

    @Override
    public User findByEmail(String email) {

        return this.userRepository.findByEmail(email);
    }

}
