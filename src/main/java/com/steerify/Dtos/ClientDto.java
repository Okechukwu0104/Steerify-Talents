package com.steerify.Dtos;

import com.steerify.Enums.Role;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
public class ClientDto {
    private UUID clientId;

    @NotBlank(message = "company name is required")
    private String companyName;

    private String firstName;
    private String lastName;

    @NotBlank(message = "Email is required")
    private String email;

    private String description;
    private String contactPerson;

    @NotBlank(message = "Phone is required")
    private String phone;

    @NotBlank(message = "Password is required")
    private String Password;

    private Role role;


    public ClientDto(UUID clientId, String companyName, String firstName, String lastName, String email, String description, String contactPerson, String phone, Role role) {
    }
}
