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
@AllArgsConstructor
@NoArgsConstructor

@Document(collection = "Clients")
public class Client implements JwtUser {
    @Id
    private UUID clientId;
    
    private String companyName;
    private String firstName;
    private String lastName;
    private String email;
    private String description;
    private String contactPerson;
    private String phone;
    private String password;
    private Role role;

    @Override
    public String getEmail() {
        return email;
    }

    @Override
    public UUID getId() {
        return clientId;
    }

    @Override
    public Role getRole() {
        return role;
    }

    @Override
    public String getPassword() {
        return password;
    }
}
