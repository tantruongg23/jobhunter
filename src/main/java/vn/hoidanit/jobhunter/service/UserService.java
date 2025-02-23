package vn.hoidanit.jobhunter.service;

import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.repository.UserRepository;

@Service
public class UserService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public UserService(UserRepository userRepository,
      PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  public User handleCreateUser(User user) {
    User newUser = new User();
    newUser.setEmail(user.getEmail());
    newUser.setName(user.getName());
    newUser.setPassword(passwordEncoder.encode(user.getPassword()));

    return this.userRepository.save(newUser);
  }

  public void handleDeleteUser(long id) {
    this.userRepository.deleteById(id);
  }

  public User fetchUserById(long id) {
    Optional<User> user = this.userRepository.findById(id);
    return user.isPresent() ? user.get() : null;
  }

  public List<User> fetchAllUsers() {
    List<User> users = this.userRepository.findAll();
    return users;
  }

  public User handleUpdateUser(User user) {
    User currentUser = this.fetchUserById(user.getId());

    if (currentUser != null) {
      currentUser.setEmail(user.getEmail());
      currentUser.setName(user.getName());
      currentUser.setPassword(passwordEncoder.encode(user.getPassword()));

      currentUser = this.userRepository.save(user);
    }

    return currentUser;
  }
}
