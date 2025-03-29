package com.steerify.Dtos.Reusables;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
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

    @NotNull(message = "Deadline is required")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate deadline;






}