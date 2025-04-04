package vn.hoidanit.jobhunter.domain.dto.response.file;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResFileUploadDTO {
    private String fileName;
    private Instant uploadedAt;

}
