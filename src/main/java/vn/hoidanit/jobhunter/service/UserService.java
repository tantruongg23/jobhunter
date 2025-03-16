package vn.hoidanit.jobhunter.service;

import java.util.List;

import vn.hoidanit.jobhunter.domain.User;

public interface UserService {
    User create(User user);

    User findOne(long id);

    List<User> findAll();

    User update(User user);

    void delete(long id);

    User findByEmail(String email);
}
