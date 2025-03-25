package com.steerify.Services;

import com.steerify.Dtos.TalentDto;
import com.steerify.Entities.Talent;
import com.steerify.Enums.Role;
import com.steerify.Helpers.LoginRequestDto;
import com.steerify.Helpers.LoginResponseDto;
import com.steerify.Repositories.JwtUserRepository;
import com.steerify.Repositories.TalentRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class TalentServicesTest {

    @Autowired
    private TalentServices talentServices;
    @Autowired
    private TalentRepository talentRepository;
    @Autowired
    private JwtUserRepository jwtUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @AfterEach
    void tearDown() {
        talentRepository.deleteAll();
        jwtUserRepository.deleteAll();
    }

    private void createTestUser() {
        Talent testUser = new Talent();
        testUser.setFirstName("Test");
        testUser.setLastName("User");
        testUser.setEmail("Steerify_Group@gmail.com");
        testUser.setTalentId(UUID.randomUUID());
        testUser.setPassword(passwordEncoder.encode("SecurePassword123!"));
        testUser.setRole(Role.TALENT);
        talentRepository.save(testUser);
    }

    @Test
    void signUp_ShouldCreateNewTalent() {
        List<String> skills = Arrays.asList("Java", "Spring Boot", "MongoDB");
        TalentDto newTalent = inputs(skills);
        TalentDto createdTalent = talentServices.signUp(newTalent);

        assertNotNull(createdTalent);
        assertNotNull(createdTalent.getTalentId());
        assertEquals("Okechukwu", createdTalent.getFirstName());
        assertEquals(skills.size(), createdTalent.getSkills().size());
        assertTrue(talentRepository.findByEmail("Steerify_Group@gmail.com").isPresent());
    }

    @Test
    void signIn_ShouldAuthenticateSuccessfully() {
        List<String> skills = Arrays.asList("Java", "Spring Boot", "MongoDB");
        TalentDto newTalent = inputs(skills);
        talentServices.signUp(newTalent);

        LoginRequestDto loginDto = new LoginRequestDto(
                "Steerify_Group@gmail.com",
                "SecurePassword123!"
        );

        LoginResponseDto response = talentServices.signIn(loginDto);
        assertNotNull(response);
        assertNotNull(response.getId());
    }



    @Test
    void signIn_ShouldFailWithWrongPassword() {
        createTestUser();

        LoginRequestDto loginDto = new LoginRequestDto(
                "Steerify_Group@gmail.com",
                "WrongPassword"
        );

        assertThrows(IllegalArgumentException.class, () -> {
            talentServices.signIn(loginDto);
        });
    }


    @Test
    void findByCompanyName() {
    }




    private static TalentDto inputs(List<String> skills) {
        TalentDto newTalent = new TalentDto();
        newTalent.setFirstName("Okechukwu");
        newTalent.setLastName("Chukwukasi");
        newTalent.setAge(23);
        newTalent.setGender("Male");
        newTalent.setEmail("Steerify_Group@gmail.com");
        newTalent.setPhoneNumber("080983765465");
        newTalent.setAddress("H112 golf estate");
        newTalent.setSkills(skills);
        newTalent.setEducation("Afe Babalola University");
        newTalent.setPassword("SecurePassword123!");
        return newTalent;
    }
}
