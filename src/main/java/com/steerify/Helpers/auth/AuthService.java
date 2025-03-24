package com.steerify.Helpers.auth;

import com.steerify.Entities.JwtUser;
import com.steerify.Helpers.LoginRequestDto;
import com.steerify.Helpers.LoginResponseDto;

public interface AuthService {
    LoginResponseDto loginUser(LoginRequestDto loginRequestDto);
}
