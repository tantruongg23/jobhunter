package vn.hoidanit.jobhunter.controller;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.service.UserService;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping()
    public User createUser(@RequestBody User user) {
        return this.userService.createUser(user);
    }

    @GetMapping("/{id}")
    public User getUserById(@PathVariable(name = "id") long id) {
        return this.userService.fetchUserById(id);
    }

    @GetMapping
    public List<User> getAllUsers() {
        return this.userService.fetchAllUsers();
    }

    @PutMapping
    public User updateUser(@RequestBody User user) {
        return this.userService.updateUser(user);
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable(name = "id") long id) {
        return this.userService.deleteUser(id);
    }

}
