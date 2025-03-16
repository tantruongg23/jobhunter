package vn.hoidanit.jobhunter.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import vn.hoidanit.jobhunter.domain.User;
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
    public User createUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    @Transactional(readOnly = true)
    public User fetchUserById(long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found for ID " + id));
    }

    @Override
    public List<User> fetchAllUsers() {
        return this.userRepository.findAll();
    }

    @Override
    public User updateUser(User updatedUser) {
        User existing = fetchUserById(updatedUser.getId()); // throws exception if not found
        existing.setName(updatedUser.getName());
        existing.setEmail(updatedUser.getEmail());
        existing.setPassword(passwordEncoder.encode(updatedUser.getPassword()));
        return userRepository.save(existing);
    }

    @Override
    public void deleteUser(long id) {
        // Optional check if user exists, or just call deleteById
        if (!userRepository.existsById(id)) {
            throw new UserNotFoundException("User does not exist for " + id);
        }
        userRepository.deleteById(id);
    }

    @Override
    public User findUserByEmail(String email) {

        return this.userRepository.findByEmail(email);
    }

}
