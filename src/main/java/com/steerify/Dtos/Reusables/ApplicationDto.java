package com.steerify.Dtos.Reusables;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.steerify.Enums.ApplicationStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class ApplicationDto {
    private UUID applicationId;
    private UUID talentId;
    private UUID jobId;

    @NotBlank(message = "Cover-Letter is required")
    private String coverLetter;
    private String companyName;
    private String jobTitle;
    private ApplicationStatus stats = ApplicationStatus.PENDING;

    public void setStatus(String status) {
        this.stats = ApplicationStatus.valueOf(status);
    }
}
