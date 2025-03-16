package vn.hoidanit.jobhunter.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;
import vn.hoidanit.jobhunter.domain.dto.LoginDTO;
import vn.hoidanit.jobhunter.domain.dto.ResLoginDTO;
import vn.hoidanit.jobhunter.response.ResponseFactory;
import vn.hoidanit.jobhunter.response.RestResponse;
import vn.hoidanit.jobhunter.util.SecurityUtil;

@RestController
public class AuthController {

    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final SecurityUtil securityUtil;

    public AuthController(AuthenticationManagerBuilder authenticationManagerBuilder,
            SecurityUtil securityUtil) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.securityUtil = securityUtil;
    }

    @PostMapping("/login")
    public ResponseEntity<RestResponse<Object>> login(@Valid @RequestBody LoginDTO loginDTO) {

        // Nạp input
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                loginDTO.getUsername(), loginDTO.getPassword());

        // Xác thực -> loadUserByUsername
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // Create a token
        String accessToken = this.securityUtil.createToken(authentication);
        // Assign user in4 to security context
        SecurityContextHolder.getContext().setAuthentication(authentication);

        ResLoginDTO resLoginDTO = new ResLoginDTO(accessToken);
        return ResponseFactory.success(resLoginDTO, "Access Token");

    }
}
