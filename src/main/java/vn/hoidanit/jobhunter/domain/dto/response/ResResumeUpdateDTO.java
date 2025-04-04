package vn.hoidanit.jobhunter.domain.dto.response;

import java.time.Instant;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ResResumeUpdateDTO {
    private long id;
    private Instant updatedAt;
    private String updatedBy;
}
