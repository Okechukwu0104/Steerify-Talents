package com.steerify.Entities;

import com.steerify.Enums.Role;
import com.steerify.Enums.TalentEnum;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

@Document(collection ="Talents" )
public class Talent implements JwtUser, UserDetails {
    @Id
    private UUID talentId;

    private String firstName;
    private String lastName;
    private int age;
    private String gender;
    private String email;
    private String phoneNumber;
    private String address;
    private List<String> Skills;
    private String education;
    private TalentEnum availability;
    private String password;
    private Role role;






    @Override
    public String getEmail() {
        return email;
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
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.getRole().name()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return this.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public String getUserType() {
        return "TALENT";
    }
}
