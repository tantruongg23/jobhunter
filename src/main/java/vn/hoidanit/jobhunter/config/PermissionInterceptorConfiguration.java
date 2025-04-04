package vn.hoidanit.jobhunter.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class PermissionInterceptorConfiguration implements WebMvcConfigurer {
    @Bean
    PermissionInterceptor getPermissionInterceptor() {
        return new PermissionInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        String[] whiteList = {
                "/",
                "/api/v1/auth/**",
                "/api/v1/companies/**",
                "/api/v1/jobs/**",
                "/api/v1/skills/**",
                "/storage/**",
                // up CV
                "/api/v1/files",
                "/api/v1/subscribers/**",
                "/api/v1/resumes/**"
        };

        registry.addInterceptor(getPermissionInterceptor())
                .excludePathPatterns(whiteList);

    }
}
