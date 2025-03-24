package com.steerify.Dtos;

import com.steerify.Enums.TalentEnum;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TalentDto {
    private UUID talentId;

    @NotBlank(message = "first and last names are required")
    private String firstName;
    private String lastName;
    private int age;
    private String gender;

    @NotBlank(message = "Email is required")
    private String emailAddress;

    private String phoneNumber;
    private String address;

    @NotBlank(message = "Skills is required")
    private List<String> Skills;

    private String education;
    private TalentEnum availability;

    @NotBlank(message = "Password is required")
    private String password;

    public TalentDto(UUID talentId, @NonNull String firstName, @NonNull String lastName, @NonNull String emailAddress, String phoneNumber, String address, @NonNull List<String> skills, String education, TalentEnum availability) {

    }
}
