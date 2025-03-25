package vn.hoidanit.jobhunter.domain.dto.response;

import java.time.Instant;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import vn.hoidanit.jobhunter.domain.dto.response.ResUserDTO.ResRoleUserDTO;
import vn.hoidanit.jobhunter.util.constant.GenderEnum;

@Setter
@Getter
@Builder
public class ResUserCreateDTO {
    private long id;
    private String name;
    private String email;
    private GenderEnum gender;
    private String address;
    private int age;
    private Instant createdAt;

    private ResCompanyUserDTO company;
    private ResRoleUserDTO role;
}
