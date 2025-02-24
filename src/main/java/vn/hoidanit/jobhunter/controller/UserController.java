package vn.hoidanit.jobhunter.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.exception.IdInvalidException;
import vn.hoidanit.jobhunter.response.ResponseFactory;
import vn.hoidanit.jobhunter.response.RestResponse;
import vn.hoidanit.jobhunter.service.UserService;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping()
    public ResponseEntity<RestResponse<User>> createUser(@RequestBody User user) {
        User createdUser = this.userService.createUser(user);
        return ResponseFactory.success(createdUser, "User created successfully", HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestResponse<User>> getUserById(@PathVariable(name = "id") String reqId)
            throws IdInvalidException {
        long id;
        try {
            id = Long.parseLong(reqId);

            User user = this.userService.fetchUserById(id);
            return ResponseFactory.success(user, "User found");
        } catch (NumberFormatException e) {
            throw new IdInvalidException("User not found for ID " + reqId);
            // No compilation error, because it's unchecked.
        }

    }

    @GetMapping
    public ResponseEntity<RestResponse<List<User>>> getAllUsers() {
        List<User> users = this.userService.fetchAllUsers();
        return ResponseFactory.success(users, "All users fetched successfully");
    }

    @PutMapping
    public ResponseEntity<RestResponse<User>> updateUser(@RequestBody User user) throws IdInvalidException {
        User updatedUser = this.userService.updateUser(user);
        return ResponseFactory.success(updatedUser, "User updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RestResponse<Void>> deleteUser(@PathVariable(name = "id") String reqId)
            throws IdInvalidException {
        long id;
        try {
            id = Long.parseLong(reqId);

            userService.deleteUser(id);
            return ResponseFactory.noContent("User deleted successfully");
        } catch (NumberFormatException e) {
            throw new IdInvalidException("User not found for ID " + reqId);
            // No compilation error, because it's unchecked.
        }

    }
}
