package vn.hoidanit.jobhunter.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.dto.PaginationResultDTO;
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
        User createdUser = this.userService.create(user);
        return ResponseFactory.success(createdUser, "User created successfully", HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestResponse<User>> getUser(@PathVariable(name = "id") String reqId)
            throws IdInvalidException {
        long id;
        try {
            id = Long.parseLong(reqId);

            User user = this.userService.findOne(id);
            return ResponseFactory.success(user, "User found");
        } catch (NumberFormatException e) {
            throw new IdInvalidException("User not found for ID " + reqId);
            // No compilation error, because it's unchecked.
        }

    }

    @GetMapping
    public ResponseEntity<RestResponse<PaginationResultDTO>> getAllUsers(
            @RequestParam("current") Optional<String> currentOptional,
            @RequestParam("pageSize") Optional<String> pageSizeOptional) {

        String sCurrent = currentOptional.orElse("");
        String sPageSize = pageSizeOptional.orElse("");

        Pageable pageable = PageRequest.of(Integer.parseInt(sCurrent) - 1, Integer.parseInt(sPageSize));

        PaginationResultDTO users = this.userService.findAll(pageable);
        return ResponseFactory.success(users, "All users fetched successfully");
    }

    @PutMapping
    public ResponseEntity<RestResponse<User>> updateUser(@RequestBody User user) throws IdInvalidException {
        User updatedUser = this.userService.update(user);
        return ResponseFactory.success(updatedUser, "User updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RestResponse<Void>> deleteUser(@PathVariable(name = "id") String reqId)
            throws IdInvalidException {
        long id;
        try {
            id = Long.parseLong(reqId);

            userService.delete(id);
            return ResponseFactory.noContent("User deleted successfully");
        } catch (NumberFormatException e) {
            throw new IdInvalidException("User not found for ID " + reqId);
            // No compilation error, because it's unchecked.
        }

    }
}
