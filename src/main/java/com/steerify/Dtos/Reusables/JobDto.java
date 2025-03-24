package com.steerify.Dtos.Reusables;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JobDto {
    private UUID jobId;
    private UUID clientId;
    private String clientName;

    @NotBlank(message = "Title is required")
    private String title;

    private String description;

    @NotNull(message = "Skills are required")
    private List<String> requiredSkills;

    @NotBlank(message = "Location is required")
    private String location;

    @NotBlank(message = "Payment amount is required")
    private String payment;

    @NotBlank(message = "Deadline is required")
    private String deadline;



}