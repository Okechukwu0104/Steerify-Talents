package com.steerify.Dtos;

import com.steerify.Enums.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[+]?[0-9]{10,15}$", message = "Invalid phone number")
    private String phone;

    @NotBlank(message = "Password is required")
    private String Password;

    private Role role;


    public ClientDto(UUID clientId, String companyName, String firstName, String lastName,
                     String email, String description, String contactPerson, String phone) {
        this.clientId = clientId;
        this.companyName = companyName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.description = description;
        this.contactPerson = contactPerson;
        this.phone = phone;
        this.role = Role.CLIENT;
    }
}
