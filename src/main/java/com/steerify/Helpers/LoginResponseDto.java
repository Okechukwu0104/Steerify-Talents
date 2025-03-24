package com.steerify.Helpers;


import com.steerify.Enums.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;
@Getter
@Setter
@AllArgsConstructor
public class LoginResponseDto {
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String token;
    private Role role;

}
