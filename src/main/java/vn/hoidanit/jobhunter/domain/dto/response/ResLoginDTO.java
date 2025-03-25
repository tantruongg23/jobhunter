package vn.hoidanit.jobhunter.domain.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.hoidanit.jobhunter.domain.Role;
import vn.hoidanit.jobhunter.util.constant.GenderEnum;

@Setter
@Getter
@NoArgsConstructor
public class ResLoginDTO {

    @JsonProperty("access_token")
    private String accessToken;

    private UserLogin user;

    public ResLoginDTO(String accessToken) {
        this.accessToken = accessToken;
    }

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class UserLogin {
        private long id;
        private String name;
        private String email;
        private GenderEnum gender;
        private Role role;
    }

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserGetAccount {
        private UserLogin user;

    }

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserInsideToken {
        private long id;
        private String name;
        private String email;
    }
}
