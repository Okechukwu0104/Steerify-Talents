package com.steerify.Entities;

import com.steerify.Enums.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
@Document(collection = "Admins")
public class Admin implements JwtUser {
    @Id
    private UUID adminId;

    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private String password;
    private Role role = Role.ADMIN;
    private String department;



    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public UUID getId() {
        return adminId;
    }

    @Override
    public Role getRole() {
        return role;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUserType() {
        return "ADMIN";
    }
}