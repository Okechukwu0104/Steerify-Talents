package com.steerify.Entities.Reusables;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "Client_job_offers")
public class Job {
    @Id
    private UUID jobId;

    private UUID clientId;
    private String clientName;
    private String title;
    private String description;
    private List<String> requiredSkills;
    private String location;
    private String payment;
    private LocalDate deadline;
}