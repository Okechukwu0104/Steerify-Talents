package com.steerify.Controllers;

import com.steerify.Dtos.ClientDto;
import com.steerify.Dtos.TalentDto;
import com.steerify.Helpers.LoginRequestDto;
import com.steerify.Helpers.LoginResponseDto;
import com.steerify.Repositories.Reusables.PostRepository;
import com.steerify.Services.TalentServices;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/talents")
public class TalentsController {
    private final TalentServices talentServices;
    private final PostRepository postRepository;

    @PostMapping("/signup")
    @PreAuthorize("permitAll()")
    ResponseEntity<TalentDto> registerResident(@Valid @RequestBody TalentDto talentDto) {
        TalentDto registeredResident = talentServices.signUp(talentDto);
        return new ResponseEntity<>(registeredResident, HttpStatus.CREATED);
    }

    @PostMapping("/login")
    @PreAuthorize("permitAll()")
    public ResponseEntity<LoginResponseDto> loginResident(@Valid @RequestBody LoginRequestDto loginDto) {
        LoginResponseDto response = talentServices.signIn(loginDto);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('TALENT')")
    public ResponseEntity<List<ClientDto>> searchForClientsCompanyName(@RequestParam String nameOfCompany) {
        List<ClientDto> foundClient = talentServices.findByCompanyName(nameOfCompany);
        return new ResponseEntity<>(foundClient, HttpStatus.OK);
    }

//    @PostMapping("/{talentId}/apply")

//    @GetMapping("/search")
//    public ResponseEntity<List<ClientDto>> searchForClientsCompanyName(
//            @RequestParam @NotBlank(message = "Company name is required") String nameOfCompany) {
//        List<ClientDto> foundClient = talentServices.findByCompanyName(nameOfCompany);
//        return new ResponseEntity<>(foundClient, HttpStatus.OK);
//    }
}