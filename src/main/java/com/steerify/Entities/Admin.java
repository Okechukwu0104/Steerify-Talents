package com.steerify.Entities;

import com.steerify.Enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.UUID;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Document(collection = "Admins")
public class Admin implements JwtUser {
    @Id
    private UUID adminId;

    private String firstName;
    private String lastName;
    private String adminEmail;
    private String adminPassword;
    private String adminPhone;
    private Role role;

    @Override
    public String getEmail() {
        return adminEmail;
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
        return adminPassword;
    }
}
