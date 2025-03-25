package com.steerify.Services;


import com.steerify.Dtos.AdminDto;
import com.steerify.Helpers.LoginRequestDto;
import com.steerify.Helpers.LoginResponseDto;

import java.util.List;
import java.util.UUID;

public interface AdminService {
    AdminDto createAdmin(AdminDto adminDto);

    LoginResponseDto login(LoginRequestDto loginRequestDto);

    AdminDto getAdminById(UUID adminId);
    List<AdminDto> getAllAdmins();
    AdminDto updateAdmin(UUID adminId, AdminDto adminDto);
    void deleteAdmin(UUID adminId);
    AdminDto findAdminByName(String adminName);
}
