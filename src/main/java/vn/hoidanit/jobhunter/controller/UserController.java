package vn.hoidanit.jobhunter.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
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

import com.turkraft.springfilter.boot.Filter;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.dto.PaginationResultDTO;
import vn.hoidanit.jobhunter.domain.dto.request.ReqUserCreateDTO;
import vn.hoidanit.jobhunter.domain.dto.response.ResUserDTO;
import vn.hoidanit.jobhunter.domain.dto.response.ResUserUpdateDTO;
import vn.hoidanit.jobhunter.exception.IdInvalidException;
import vn.hoidanit.jobhunter.response.ResponseFactory;
import vn.hoidanit.jobhunter.response.RestResponse;
import vn.hoidanit.jobhunter.service.UserService;
import vn.hoidanit.jobhunter.util.annotation.ApiMessage;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping()
    public ResponseEntity<RestResponse<ReqUserCreateDTO>> createUser(@RequestBody User user) {
        ReqUserCreateDTO createdUser = this.userService.create(user);
        return ResponseFactory.success(createdUser, "User created successfully", HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<RestResponse<ResUserDTO>> getUser(@PathVariable(name = "id") String reqId)
            throws IdInvalidException {
        long id;
        try {
            id = Long.parseLong(reqId);

            ResUserDTO user = this.userService.findOne(id);
            return ResponseFactory.success(user, "User found");
        } catch (NumberFormatException e) {
            throw new IdInvalidException("User not found for ID " + reqId);
            // No compilation error, because it's unchecked.
        }

    }

    @GetMapping
    @ApiMessage(value = "fetch all users")
    public ResponseEntity<RestResponse<PaginationResultDTO>> getAllUsers(
            @Filter Specification<User> specification,
            Pageable pageable) {

        PaginationResultDTO users = this.userService.findAll(specification, pageable);
        return ResponseFactory.success(users, "Users fetched successfully");
    }

    @PutMapping
    public ResponseEntity<RestResponse<ResUserUpdateDTO>> updateUser(@Valid @RequestBody ResUserUpdateDTO user)
            throws IdInvalidException {
        ResUserUpdateDTO updatedUser = this.userService.update(user);
        return ResponseFactory.success(updatedUser, "User updated successfully");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<RestResponse<Void>> deleteUser(@PathVariable(name = "id") String reqId)
            throws IdInvalidException {
        long id;
        try {
            id = Long.parseLong(reqId);

            userService.delete(id);
            return ResponseFactory.success(null, "User deleted successfully", HttpStatus.OK);
        } catch (NumberFormatException e) {
            throw new IdInvalidException("User not found for ID " + reqId);
            // No compilation error, because it's unchecked.
        }

    }
}
