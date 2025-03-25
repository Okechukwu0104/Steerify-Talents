package com.steerify.Dtos;

import com.steerify.Enums.TalentEnum;
import jakarta.validation.constraints.*;
import lombok.*;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TalentDto {
    private UUID talentId;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    private int age;

    @NotBlank(message = "Pls enter your gender")
    private String gender;

    @Email(message = "Email is required")
    private String email;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[+]?[0-9]{10,15}$", message = "Invalid phone number")
    private String phoneNumber;
    private String address;

    @NotEmpty(message = "At least one skill is required")
    private List<String> skills;

    @NotBlank(message = "Pls input your education" )
    private String education;
    private TalentEnum availability;

    @NotBlank(message = "Password is required")
    @NotNull(message = "Password cannot be null")
    private String password;

    public TalentDto(UUID talentId, String firstName, String lastName, int age,String gender, String emailAddress, String phoneNumber, String address, List<String> skills, String education, TalentEnum availability) {
        this.talentId = talentId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.gender = gender;
        this.email = emailAddress;
        this.phoneNumber = phoneNumber;
        this.address = address;
        this.skills = skills;
        this.education = education;
        this.availability = availability;
    }
}
