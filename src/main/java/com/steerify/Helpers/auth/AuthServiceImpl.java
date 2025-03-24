package com.steerify.Helpers.auth;

import com.steerify.Entities.JwtUser;
import com.steerify.Helpers.JwtService.JwtService;
import com.steerify.Helpers.LoginRequestDto;
import com.steerify.Helpers.LoginResponseDto;
import com.steerify.Repositories.JwtUserRepository;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    private final JwtUserRepository jwtUserRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public AuthServiceImpl(JwtUserRepository jwtUserRepository, JwtService jwtService) {
        this.jwtUserRepository = jwtUserRepository;
        this.jwtService = jwtService;
    }


    @Override
    public LoginResponseDto loginUser(LoginRequestDto loginRequestDto) {
        String email = loginRequestDto.getEmail();
        String password = loginRequestDto.getPassword();
        JwtUser loggedInUser = jwtUserRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!passwordEncoder.matches(password, loggedInUser.getPassword())) {
            throw new BadCredentialsException("Wrong password");
        }

        return jwtService.generateToken(loggedInUser);
    }
}