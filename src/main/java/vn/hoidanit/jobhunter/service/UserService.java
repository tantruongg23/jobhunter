package vn.hoidanit.jobhunter.service;

import java.util.List;

import vn.hoidanit.jobhunter.domain.User;

public interface UserService {
    User createUser(User user);

    User fetchUserById(long id);

    List<User> fetchAllUsers();

    User updateUser(User user);

    String deleteUser(long id);
}
