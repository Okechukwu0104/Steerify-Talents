package com.steerify.Helpers.config;

import com.steerify.Entities.Admin;
import com.steerify.Entities.Client;
import com.steerify.Entities.JwtUser;
import com.steerify.Entities.Talent;
import com.steerify.Repositories.AdminRepository;
import com.steerify.Repositories.ClientRepository;
import com.steerify.Repositories.TalentRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final AdminRepository adminRepository;
    private final TalentRepository talentRepository;
    private final ClientRepository clientRepository;

    public CustomUserDetailsService(
            AdminRepository adminRepository,
            TalentRepository talentRepository,
            ClientRepository clientRepository) {
        this.adminRepository = adminRepository;
        this.talentRepository = talentRepository;
        this.clientRepository = clientRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Admin admin = adminRepository.findByAdminEmail(email).orElse(null);
        if (admin != null) {
            return createUserDetails(admin);
        }

        Talent talent = talentRepository.findByEmailAddress(email).orElse(null);
        if (talent != null) {
            return createUserDetails(talent);
        }

        Client client = clientRepository.findByEmail(email).orElse(null);
        if (client != null) {
            return createUserDetails(client);
        }

        throw new UsernameNotFoundException("User not found with email: " + email);
    }

    private UserDetails createUserDetails(JwtUser user) {
        return User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
    }
}
