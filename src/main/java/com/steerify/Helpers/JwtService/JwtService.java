package com.steerify.Helpers.JwtService;

import com.steerify.Entities.JwtUser;
import com.steerify.Helpers.LoginResponseDto;


public interface JwtService {
    LoginResponseDto generateToken(JwtUser loggedInUser) ;
    String extractUsername(String token) ;
    Boolean validateToken(String token, JwtUser user) ;

}
