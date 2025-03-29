package com.steerify.Services.impl;

import com.steerify.Enums.Role;
import com.steerify.Helpers.LoginRequestDto;
import com.steerify.Helpers.LoginResponseDto;
import com.steerify.Helpers.auth.AuthService;
import com.steerify.Repositories.JwtUserRepository;
import com.steerify.Services.AdminService;
import com.steerify.Dtos.AdminDto;
import com.steerify.Entities.Admin;
import com.steerify.Mappers.AdminMapper;
import com.steerify.Repositories.AdminRepository;
import com.steerify.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUserRepository jwtUserRepository;
    private final AuthService authService;


    @Override
    public AdminDto createAdmin(AdminDto adminDto) {
        if( !adminRepository.existsByEmail(adminDto.getEmail()) ) {
            Admin admin = AdminMapper.mapToAdmin(adminDto);
            admin.setRole(Role.ADMIN);
            Admin savedAdmin = adminRepository.save(admin);
            jwtUserRepository.save(savedAdmin);
            return AdminMapper.mapToAdminDto(savedAdmin);
        }
        throw new ResourceNotFoundException("Client with email: "+adminDto.getEmail()+" already exists");

    }

    @Override
    public LoginResponseDto login(LoginRequestDto loginRequestDto) {
        return authService.loginUser(loginRequestDto);
    }

    @Override
    public AdminDto getAdminById(UUID adminId) {
        Admin admin = adminRepository.findById(adminId)
                .orElseThrow(() -> new IllegalArgumentException("Admin not found"));
        return AdminMapper.mapToAdminDto(admin);
    }

    @Override
    public List<AdminDto> getAllAdmins() {
        return adminRepository.findAll().stream()
                .map(AdminMapper::mapToAdminDto)
                .collect(Collectors.toList());
    }

    @Override
    public AdminDto updateAdmin(UUID adminId, AdminDto adminDto) {
        Admin existingAdmin = adminRepository.findById(adminId)
                .orElseThrow(() -> new IllegalArgumentException("Admin not found"));

        existingAdmin.setFirstName(adminDto.getFirstName());
        existingAdmin.setLastName(adminDto.getLastName());
        existingAdmin.setPhone(adminDto.getPhone());
        existingAdmin.setDepartment(adminDto.getDepartment());

        if (adminDto.getPassword() != null && !adminDto.getPassword().isEmpty()) {
            existingAdmin.setPassword(passwordEncoder.encode(adminDto.getPassword()));
        }

        Admin updatedAdmin = adminRepository.save(existingAdmin);
        return AdminMapper.mapToAdminDto(updatedAdmin);
    }

    @Override
    public void deleteAdmin(UUID adminId) {
        adminRepository.deleteById(adminId);
    }


    @Override
    public AdminDto findAdminByName(String adminName) {
        Optional<Admin> byFirstName = adminRepository.findByFirstNameIgnoreCase(adminName);
        List<Admin> byLastName = adminRepository.findByLastNameContainingIgnoreCase(adminName);

        if (byFirstName.isPresent()) {
            return AdminMapper.mapToAdminDto(byFirstName.get());
        }

        if (!byLastName.isEmpty()) {
            return AdminMapper.mapToAdminDto(byLastName.get(0));
        }
        throw new IllegalArgumentException("No admin found with name: " + adminName);
    }
}
