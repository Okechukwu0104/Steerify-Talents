package com.steerify.Helpers.auth;

import com.steerify.Entities.Admin;
import com.steerify.Entities.Client;
import com.steerify.Entities.JwtUser;
import com.steerify.Entities.Talent;
import com.steerify.Helpers.JwtService.JwtService;
import com.steerify.Helpers.LoginRequestDto;
import com.steerify.Helpers.LoginResponseDto;
import com.steerify.Repositories.AdminRepository;
import com.steerify.Repositories.ClientRepository;
import com.steerify.Repositories.JwtUserRepository;
import com.steerify.Repositories.TalentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final JwtUserRepository jwtUserRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    private final AdminRepository adminRepository;
    private final TalentRepository talentRepository;
    private final ClientRepository clientRepository;



    @Override
    public LoginResponseDto loginUser(LoginRequestDto request) {

        if (request.getEmail() == null || request.getPassword() == null) {
            throw new IllegalArgumentException("Email and password are required");
        }
        Talent talent = talentRepository.findByEmail(request.getEmail()).orElse(null);
        Admin admin = adminRepository.findByEmail(request.getEmail()).orElse(null);
        Client client = clientRepository.findByEmail(request.getEmail()).orElse(null);


        if (talent != null) {
            if (!passwordEncoder.matches(request.getPassword(), talent.getPassword())) {
                throw new IllegalArgumentException("Invalid password for talent");
            }
            request.setUserType("TALENT");
        } else if (admin != null) {
            if (!passwordEncoder.matches(request.getPassword(), admin.getPassword())) {
                throw new IllegalArgumentException("Invalid password for admin");
            }
            request.setUserType("ADMIN");
        } else if (client != null) {
            if (!passwordEncoder.matches(request.getPassword(), client.getPassword())) {
                throw new IllegalArgumentException("Invalid password for client");
            }
            request.setUserType("CLIENT");
        } else {
            throw new IllegalArgumentException("User not found with email: " + request.getEmail());
        }

        String email = request.getEmail();
        String password = request.getPassword();
        JwtUser loggedInUser = jwtUserRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("in auth service impl User not found"));

        if (!passwordEncoder.matches(password, loggedInUser.getPassword())) {
            throw new BadCredentialsException("Wrong password");
        }

        return jwtService.generateToken(loggedInUser);
    }
}