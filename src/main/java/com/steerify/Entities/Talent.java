package com.steerify.Entities;

import com.steerify.Enums.Role;
import com.steerify.Enums.TalentEnum;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Document(collection ="Talents" )
public class Talent implements JwtUser{
    @Id
    private UUID talentId;

    private String firstName;
    private String lastName;
    private int age;
    private String gender;
    private String emailAddress;
    private String phoneNumber;
    private String address;
    private List<String> Skills;
    private String education;
    private TalentEnum availability;
    private String password;
    private Role role;



    public Talent(UUID talentId, String firstName, String lastName, int age, String gender, String emailAddress, String phoneNumber, String address, List<String> skills, String education, TalentEnum availability, String password) {
    }

    @Override
    public String getEmail() {
        return emailAddress;
    }

    @Override
    public UUID getId() {
        return talentId;
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
