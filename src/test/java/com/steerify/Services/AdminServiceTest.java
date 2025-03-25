package com.steerify.Services;

import com.steerify.Dtos.AdminDto;
import com.steerify.Entities.Admin;
import com.steerify.Enums.Role;
import com.steerify.Repositories.AdminRepository;
import com.steerify.Services.impl.AdminServiceImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataMongoTest
@Import({AdminServiceImpl.class, BCryptPasswordEncoder.class})
class AdminServiceImplIntegrationTest {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private AdminService adminService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private AdminDto testAdminDto;
    private Admin existingAdmin;

    @BeforeEach
    void setUp() {
        testAdminDto = new AdminDto();
        testAdminDto.setFirstName("Test");
        testAdminDto.setLastName("Admin");
        testAdminDto.setEmail("test@tms.com");
        testAdminDto.setPhone("+1234567890");
        testAdminDto.setPassword("SecurePass123!");
        testAdminDto.setDepartment("HR");

        existingAdmin = new Admin();
        existingAdmin.setAdminId(UUID.randomUUID());
        existingAdmin.setFirstName("Existing");
        existingAdmin.setEmail("existing@tms.com");
        existingAdmin.setPassword(passwordEncoder.encode("existingPass"));
        adminRepository.save(existingAdmin);
    }

    @AfterEach
    void cleanUp() {
        adminRepository.deleteAll();
    }

    @Test
    void createAdmin_ShouldPersistAdminWithEncryptedPassword() {
        AdminDto result = adminService.createAdmin(testAdminDto);

        Admin savedAdmin = adminRepository.findById(result.getAdminId()).orElseThrow();
        assertEquals("Test", savedAdmin.getFirstName());
        assertTrue(passwordEncoder.matches("SecurePass123!", savedAdmin.getPassword()));
        assertEquals(Role.ADMIN, savedAdmin.getRole());
    }

    @Test
    void createAdmin_ShouldRejectDuplicateEmail() {
        adminService.createAdmin(testAdminDto);

        AdminDto duplicateDto = new AdminDto();
        duplicateDto.setEmail("test@tms.com");

        assertThrows(IllegalArgumentException.class, () ->
                adminService.createAdmin(duplicateDto));
    }

    @Test
    void getAdminById_ShouldReturnAdmin_WhenExists() {
        AdminDto result = adminService.getAdminById(existingAdmin.getAdminId());

        assertEquals(existingAdmin.getAdminId(), result.getAdminId());
        assertEquals("Existing", result.getFirstName());
        assertNull(result.getPassword());
    }

    @Test
    void getAdminById_ShouldThrow_WhenNotFound() {
        assertThrows(IllegalArgumentException.class, () ->
                adminService.getAdminById(UUID.randomUUID()));
    }

    @Test
    void getAllAdmins_ShouldReturnAllAdmins() {
        Admin secondAdmin = new Admin();
        secondAdmin.setAdminId(UUID.randomUUID());
        secondAdmin.setEmail("second@tms.com");
        adminRepository.save(secondAdmin);

        List<AdminDto> results = adminService.getAllAdmins();

        assertTrue(results.stream().anyMatch(a -> a.getEmail().equals("existing@tms.com")));
        assertTrue(results.stream().anyMatch(a -> a.getEmail().equals("second@tms.com")));
    }

    @Test
    void updateAdmin_ShouldModifyNonCredentialFields() {
        AdminDto update = new AdminDto();
        update.setFirstName("Updated");
        update.setPhone("+9876543210");
        update.setDepartment("Finance");

        AdminDto result = adminService.updateAdmin(existingAdmin.getAdminId(), update);

        assertEquals("Updated", result.getFirstName());
        assertEquals("+9876543210", result.getPhone());
        assertEquals("Finance", result.getDepartment());

        Admin updatedAdmin = adminRepository.findById(existingAdmin.getAdminId()).orElseThrow();
        assertEquals("existing@tms.com", updatedAdmin.getEmail());
    }

    @Test
    void updateAdmin_ShouldEncodeNewPassword() {
        AdminDto update = new AdminDto();
        update.setPassword("NewSecurePass123!");

        adminService.updateAdmin(existingAdmin.getAdminId(), update);

        Admin updatedAdmin = adminRepository.findById(existingAdmin.getAdminId()).orElseThrow();
        assertTrue(passwordEncoder.matches("NewSecurePass123!", updatedAdmin.getPassword()));
    }

    @Test
    void updateAdmin_ShouldIgnoreNullPassword() {
        String originalHash = existingAdmin.getPassword();
        AdminDto update = new AdminDto();
        update.setFirstName("Updated");

        adminService.updateAdmin(existingAdmin.getAdminId(), update);

        Admin updatedAdmin = adminRepository.findById(existingAdmin.getAdminId()).orElseThrow();
        assertEquals(originalHash, updatedAdmin.getPassword());
    }

    @Test
    void deleteAdmin_ShouldRemoveFromDatabase() {
        adminService.deleteAdmin(existingAdmin.getAdminId());
        assertFalse(adminRepository.existsById(existingAdmin.getAdminId()));
    }



    @Test
    void findAdminByName_ShouldFindByFirstNameOrLastName() {
        Admin johnDoe = new Admin();
        johnDoe.setAdminId(UUID.randomUUID());
        johnDoe.setFirstName("John");
        johnDoe.setLastName("Doe");
        johnDoe.setEmail("john@tms.com");
        johnDoe.setPassword("password");
        johnDoe.setRole(Role.ADMIN);
        adminRepository.save(johnDoe);

        Admin janeSmith = new Admin();
        janeSmith.setAdminId(UUID.randomUUID());
        janeSmith.setFirstName("Jane");
        janeSmith.setLastName("Smith");
        janeSmith.setEmail("jane@tms.com");
        janeSmith.setPassword("password");
        janeSmith.setRole(Role.ADMIN);
        adminRepository.save(janeSmith);

        AdminDto foundJohn = adminService.findAdminByName("John");
        assertNotNull(foundJohn);
        assertEquals("john@tms.com", foundJohn.getEmail());

        AdminDto foundSmith = adminService.findAdminByName("Smit");
        assertNotNull(foundSmith);
        assertEquals("jane@tms.com", foundSmith.getEmail());
    }
}