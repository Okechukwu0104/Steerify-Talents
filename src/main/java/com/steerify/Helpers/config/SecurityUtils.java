package com.steerify.Helpers.config;

import com.steerify.Repositories.JwtUserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class SecurityUtils {

    private final JwtUserRepository jwtUserRepository;

    public SecurityUtils(JwtUserRepository jwtUserRepository) {
        this.jwtUserRepository = jwtUserRepository;
    }

//    public UUID getCurrentUserId() {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
//            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
//            return jwtUserRepository.findByuserDetails.;
//        }
//        throw new IllegalStateException("User not authenticated");
//    }

    public String getCurrentUserEmail() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetails) {
            return ((UserDetails) authentication.getPrincipal()).getUsername();
        }
        throw new IllegalStateException("User not authenticated");
    }

    public String getCurrentUserRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && !authentication.getAuthorities().isEmpty()) {
            return authentication.getAuthorities().iterator().next().getAuthority();
        }
        throw new IllegalStateException("User role not found");
    }
}
