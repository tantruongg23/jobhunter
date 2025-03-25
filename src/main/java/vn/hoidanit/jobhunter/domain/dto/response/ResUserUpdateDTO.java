package vn.hoidanit.jobhunter.domain.dto.response;

import java.time.Instant;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.hoidanit.jobhunter.domain.dto.response.ResUserDTO.ResRoleUserDTO;
import vn.hoidanit.jobhunter.util.constant.GenderEnum;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResUserUpdateDTO {
    private long id;
    @NotBlank(message = "Name must not be empty")
    private String name;
    @NotNull(message = "Gender must not be empty")
    private GenderEnum gender;
    @NotBlank(message = "Address must not be empty")
    private String address;
    @NotNull(message = "Age must not be empty")
    private int age;
    private Instant updatedAt;

    private ResCompanyUserDTO company;
    private ResRoleUserDTO role;
}
