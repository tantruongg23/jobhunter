package vn.hoidanit.jobhunter.domain.dto.response;

import lombok.Getter;
import lombok.Setter;
import vn.hoidanit.jobhunter.util.constant.GenderEnum;

@Setter
@Getter
public class ResUserLoginDTO {
    private long id;
    private String name;
    private String email;
    private GenderEnum gender;
}
