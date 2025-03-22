package vn.hoidanit.jobhunter.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.domain.dto.request.ReqLoginDTO;
import vn.hoidanit.jobhunter.domain.dto.response.ResLoginDTO;
import vn.hoidanit.jobhunter.exception.IdInvalidException;
import vn.hoidanit.jobhunter.response.ResponseFactory;
import vn.hoidanit.jobhunter.response.RestResponse;
import vn.hoidanit.jobhunter.service.UserService;
import vn.hoidanit.jobhunter.util.SecurityUtil;
import vn.hoidanit.jobhunter.util.constant.GenderEnum;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final SecurityUtil securityUtil;
    private final UserService userService;

    @Value("${hoidanit.jwt.refresh-token-validity-in-seconds}")
    private long refreshTokenExpiration;

    public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder,
            SecurityUtil securityUtil,
            UserService userService) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.securityUtil = securityUtil;
        this.userService = userService;
    }

    @PostMapping("/login")
    public ResponseEntity<RestResponse<ResLoginDTO>> login(@Valid @RequestBody ReqLoginDTO loginDTO) {

        // Nạp input
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginDTO.getUsername(), loginDTO.getPassword());

        // Xác thực -> loadUserByUsername
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // Assign user in4 to security context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        ResLoginDTO resLoginDTO = new ResLoginDTO();

        User currentUser = this.userService.findByEmail(loginDTO.getUsername());
        if (currentUser == null) {
            throw new IdInvalidException("Username is not valid");
        }

        ResLoginDTO.UserLogin userLogin = ResLoginDTO.UserLogin
                .builder()
                .id(currentUser.getId())
                .email(currentUser.getEmail())
                .name(currentUser.getName())
                .gender(currentUser.getGender())
                .build();
        resLoginDTO.setUser(userLogin);

        // Create a token
        String accessToken = this.securityUtil.createAccessToken(authentication.getName(), resLoginDTO.getUser());
        resLoginDTO.setAccessToken(accessToken);
        // Create refresh token
        String refreshToken = this.securityUtil.createRefreshToken(loginDTO.getUsername(), resLoginDTO);

        // update user
        this.userService.updateUserToken(refreshToken, loginDTO.getUsername());

        // set cookies
        ResponseCookie resCookies = ResponseCookie
                .from("refresh_token", refreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(refreshTokenExpiration)
                .build();

        return ResponseFactory.success(resLoginDTO, "Login successfully", HttpStatus.OK, resCookies.toString());

    }

    @GetMapping("/account")
    public ResponseEntity<RestResponse<ResLoginDTO.UserGetAccount>> getAccount() {
        String email = SecurityUtil.getCurrentUserLogin().orElse("");

        User currentUser = this.userService.findByEmail(email);

        if (currentUser == null) {
            return ResponseFactory.error("User is not found", HttpStatus.BAD_REQUEST, currentUser);
        }

        ResLoginDTO.UserLogin userLogin = ResLoginDTO.UserLogin.builder()
                .id(currentUser.getId())
                .email(currentUser.getEmail())
                .name(currentUser.getName())
                .gender(currentUser.getGender())
                .build();

        ResLoginDTO.UserGetAccount userGetAccount = new ResLoginDTO.UserGetAccount(userLogin);

        return ResponseFactory.success(userGetAccount, "Get account successfully");
    }

    @GetMapping("/refresh")
    public ResponseEntity<RestResponse<ResLoginDTO>> getNewToken(
            @CookieValue(name = "refresh_token", defaultValue = "") String refreshToken) {

        // Check if token valid
        Jwt decodedToken = this.securityUtil.isValidRefreshToken(refreshToken);
        String email = decodedToken.getSubject();

        // Check user by refresh token + email
        User currentUser = this.userService.getUserByRefreshTokenAndEmail(refreshToken, email);
        if (currentUser == null) {
            throw new IdInvalidException("Refresh token is not valid");
        }

        // Issue new token and response
        ResLoginDTO resLoginDTO = new ResLoginDTO();

        if (currentUser != null) {
            ResLoginDTO.UserLogin userLogin = ResLoginDTO.UserLogin
                    .builder()
                    .id(currentUser.getId())
                    .email(currentUser.getEmail())
                    .name(currentUser.getName())
                    .gender(currentUser.getGender())
                    .build();
            resLoginDTO.setUser(userLogin);
        }

        // Create a token
        String accessToken = this.securityUtil.createAccessToken(email, resLoginDTO.getUser());
        resLoginDTO.setAccessToken(accessToken);
        // Create refresh token
        String newRefreshToken = this.securityUtil.createRefreshToken(email, resLoginDTO);

        // update user
        this.userService.updateUserToken(newRefreshToken, email);

        // set cookies
        ResponseCookie resCookies = ResponseCookie
                .from("refresh_token", newRefreshToken)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(refreshTokenExpiration)
                .build();

        return ResponseFactory.success(resLoginDTO, "Get a new token successfully", HttpStatus.OK,
                resCookies.toString());

    }

    @PostMapping("/logout")
    public ResponseEntity<RestResponse<Void>> logout() throws IdInvalidException {
        // TODO: process POST request
        String email = SecurityUtil.getCurrentUserLogin().orElse("");
        if (email.equals("")) {
            throw new IdInvalidException("Access token is not valid");
        }

        // update refresh token
        this.userService.updateUserToken(null, email);
        // remove refresh token
        ResponseCookie deleteResponseCookie = ResponseCookie
                .from("refresh_token", null)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .build();

        return ResponseFactory.success(null, "Logout successfully", HttpStatus.OK, deleteResponseCookie.toString());
    }

}
