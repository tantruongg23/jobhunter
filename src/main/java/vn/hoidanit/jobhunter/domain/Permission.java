package vn.hoidanit.jobhunter.domain;

import java.time.Instant;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.hoidanit.jobhunter.util.SecurityUtil;

@Entity
@Table(name = "permissions")
@Setter
@Getter
@NoArgsConstructor
public class Permission {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NotBlank(message = "Name must be not empty")
    private String name;
    @NotBlank(message = "Api path must be not empty")
    private String apiPath;
    @NotBlank(message = "Method must be not empty")
    private String method;
    @NotBlank(message = "Module must be not empty")
    private String module;

    @ManyToMany(mappedBy = "permissions", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Role> roles;

    private Instant createdAt;
    private Instant updatedAt;
    private String createdBy;
    private String updatedBy;

    public Permission(@NotBlank(message = "Name must be not empty") String name,
            @NotBlank(message = "Api path must be not empty") String apiPath,
            @NotBlank(message = "Method must be not empty") String method,
            @NotBlank(message = "Module must be not empty") String module) {
        this.name = name;
        this.apiPath = apiPath;
        this.method = method;
        this.module = module;
    }

    @PrePersist
    public void handlePrePersist() {
        this.createdAt = Instant.now();
        this.createdBy = SecurityUtil.getCurrentUserLogin().orElse(null);
    }

    @PreUpdate
    public void handlePreUpdate() {
        this.updatedAt = Instant.now();
        this.updatedBy = SecurityUtil.getCurrentUserLogin().orElse(null);
    }
}
