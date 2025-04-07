package com.steerify.Entities.Reusables;

import com.steerify.Enums.ApplicationStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Document(collection = "Job_Applications")
public class Application {
    @Id
    private UUID applicationId;

    private UUID talentId;
    private UUID jobId;
    private String coverLetter;
    private String companyName;
    private String jobTitle;
    private ApplicationStatus stats;



}
