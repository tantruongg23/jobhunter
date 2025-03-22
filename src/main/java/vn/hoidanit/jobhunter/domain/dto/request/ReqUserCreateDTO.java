package vn.hoidanit.jobhunter.domain.dto.request;

import java.time.Instant;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import vn.hoidanit.jobhunter.util.constant.GenderEnum;

@Setter
@Getter
@Builder
public class ReqUserCreateDTO {
    private long id;
    private String name;
    private String email;
    private GenderEnum gender;
    private String address;
    private int age;
    private Instant createdAt;
}
