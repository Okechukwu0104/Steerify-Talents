package com.steerify.Helpers.config;

import com.steerify.Entities.Admin;
import com.steerify.Entities.Client;
import com.steerify.Entities.JwtUser;
import com.steerify.Entities.Talent;
import com.steerify.Repositories.AdminRepository;
import com.steerify.Repositories.ClientRepository;
import com.steerify.Repositories.TalentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final AdminRepository adminRepository;
    private final TalentRepository talentRepository;
    private final ClientRepository clientRepository;


    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Admin admin = adminRepository.findByEmail(email).orElse(null);
        if (admin != null) return admin;

        Talent talent = talentRepository.findByEmail(email).orElse(null);
        if (talent != null) return talent;

        Client client = clientRepository.findByEmail(email).orElse(null);
        if (client != null) return client;

        throw new UsernameNotFoundException("User not found with email: " + email);
    }
}
