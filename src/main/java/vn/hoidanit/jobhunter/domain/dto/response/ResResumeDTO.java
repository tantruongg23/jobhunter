package vn.hoidanit.jobhunter.domain.dto.response;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;
import vn.hoidanit.jobhunter.util.constant.StatusEnum;

@Setter
@Getter
public class ResResumeDTO {
    private long id;
    private String email;
    private String url;
    private StatusEnum status;

    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;

    private ResumeUserDTO user;
    private ResumeJobDTO job;

    @Setter
    @Getter
    public static class ResumeUserDTO {
        private long id;
        private String name;
    }

    @Setter
    @Getter
    public static class ResumeJobDTO {
        private long id;
        private String name;
    }
}
