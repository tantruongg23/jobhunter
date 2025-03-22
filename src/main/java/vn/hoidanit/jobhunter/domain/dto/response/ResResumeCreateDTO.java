package vn.hoidanit.jobhunter.domain.dto.response;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResResumeCreateDTO {
    private long id;
    private Instant createdAt;
    private String createdBy;
}
