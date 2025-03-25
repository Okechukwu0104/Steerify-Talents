package com.steerify.Helpers;

import com.steerify.Enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LoginRequestDto {
    private String email;
    private String password;
    private Role role;

    public LoginRequestDto(String email, String password) {
        this.email = email;
        this.password = password;
    }
    public void setUserType(String talent) {
        this.role = Role.valueOf(talent);
    }
}
