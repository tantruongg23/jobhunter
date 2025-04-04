package vn.hoidanit.jobhunter.config;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import vn.hoidanit.jobhunter.domain.Permission;
import vn.hoidanit.jobhunter.domain.Role;
import vn.hoidanit.jobhunter.domain.User;
import vn.hoidanit.jobhunter.exception.IdInvalidException;
import vn.hoidanit.jobhunter.exception.PermissionException;
import vn.hoidanit.jobhunter.service.UserService;
import vn.hoidanit.jobhunter.util.SecurityUtil;

public class PermissionInterceptor implements HandlerInterceptor {

    @Autowired
    private UserService userService;

    @Override
    @Transactional
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {

        // Define in controller
        String path = (String) request.getAttribute(HandlerMapping.BEST_MATCHING_PATTERN_ATTRIBUTE);
        // Client request url
        String requestURI = request.getRequestURI();
        // Client request method
        String httpMethod = request.getMethod();

        System.out.println(">>>>> RUN Prehandler");
        System.out.println(">>> path =" + path);
        System.out.println(">>>> URI = " + requestURI);
        System.out.println(">>>>> Method = " + httpMethod);

        // query client's permission:
        // if match url + method -> true
        String email = SecurityUtil.getCurrentUserLogin().orElse(null);
        if (email != null) {
            User user = this.userService.findByEmail(email);
            if (user != null) {
                // @Transactional - keep session to DB
                Role role = user.getRole();
                if (role != null) {
                    List<Permission> permissions = role.getPermissions();

                    boolean isAllow = permissions.stream()
                            .anyMatch(item -> item.getApiPath().equals(path)
                                    && item.getMethod().equals(httpMethod));

                    if (!isAllow) {
                        throw new PermissionException("You don't have rights to access this endpoint");
                    }
                } else {
                    throw new PermissionException("You don't have rights to access this api");
                }
            }

        }

        return true;
    }

}
