package vn.hoidanit.jobhunter.service.impl;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.repository.UserRepository;
import vn.hoidanit.jobhunter.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public User createUser(User user) {

        User newUser = this.userRepository.save(user);
        return newUser;
    }

    @Override
    public User fetchUserById(long id) {
        Optional<User> user = this.userRepository.findById(id);
        if (user.isEmpty()) {
            return null;
        }

        return user.get();
    }

    @Override
    public List<User> fetchAllUsers() {
        return this.userRepository.findAll();
    }

    @Override
    public User updateUser(User user) {
        User currentUser = fetchUserById(user.getId());
        if (currentUser == null) {
            return currentUser;
        }

        currentUser.setName(user.getName());
        currentUser.setEmail(user.getEmail());
        currentUser.setPassword(user.getPassword());

        return this.userRepository.save(currentUser);
    }

    @Override
    public String deleteUser(long id) {
        Optional<User> user = this.userRepository.findById(id);
        if (user.isEmpty()) {
            return "User not found!!";
        }

        this.userRepository.deleteById(id);
        return "User has been deleted successfully!";
    }
}
