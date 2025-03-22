package vn.hoidanit.jobhunter.domain.dto.response;

import java.time.Instant;
import java.util.List;

import lombok.Getter;
import lombok.Setter;
import vn.hoidanit.jobhunter.util.constant.LevelEnum;

@Setter
@Getter
public class ResJobUpdateDTO {

    long id;

    private String name;
    private String location;
    private Double salary;
    private int quantity;
    private LevelEnum level;

    private String description;

    private Instant startDate;
    private Instant endDate;
    private boolean active;

    private List<String> skills;

    private Instant updatedAt;
    private String updatedBy;
}
