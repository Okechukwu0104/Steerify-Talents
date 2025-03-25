package com.steerify.Entities;


import com.steerify.Enums.Role;

import java.util.UUID;

public interface JwtUser {
    String getEmail();
    UUID getId();
    String getFirstName();
    String getLastName();
    Role getRole();
    String getPassword();
    String getUserType();

}
