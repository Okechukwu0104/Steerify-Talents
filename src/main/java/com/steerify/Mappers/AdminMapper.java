package com.steerify.Mappers;


import com.steerify.Dtos.AdminDto;
import com.steerify.Entities.Admin;

import java.util.UUID;

public class AdminMapper {
    public static Admin mapToAdmin(AdminDto adminDto) {
        return new Admin(
                adminDto.getAdminId() != null ? adminDto.getAdminId() : UUID.randomUUID(),
                adminDto.getFirstName(),
                adminDto.getLastName(),
                adminDto.getEmail(),
                adminDto.getPhone(),
                adminDto.getPassword(),
                adminDto.getRole(),
                adminDto.getDepartment()
        );
    }

    public static AdminDto mapToAdminDto(Admin admin) {
        return new AdminDto(
                admin.getAdminId(),
                admin.getFirstName(),
                admin.getLastName(),
                admin.getEmail(),
                admin.getPhone(),
                admin.getRole(),
                admin.getDepartment()
        );
    }
}
