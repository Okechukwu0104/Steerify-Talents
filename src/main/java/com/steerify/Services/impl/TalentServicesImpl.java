package com.steerify.Services.impl;


import com.steerify.Dtos.ClientDto;
import com.steerify.Dtos.TalentDto;
import com.steerify.Entities.Admin;
import com.steerify.Entities.Client;
import com.steerify.Entities.Talent;
import com.steerify.Enums.Role;
import com.steerify.Helpers.LoginRequestDto;
import com.steerify.Helpers.LoginResponseDto;
import com.steerify.Helpers.auth.AuthService;
import com.steerify.Mappers.AdminMapper;
import com.steerify.Mappers.ClientMapper;
import com.steerify.Repositories.AdminRepository;
import com.steerify.Repositories.ClientRepository;
import com.steerify.Enums.TalentEnum;
import com.steerify.Mappers.TalentMapper;
import com.steerify.Repositories.JwtUserRepository;
import com.steerify.Repositories.TalentRepository;
import com.steerify.Services.TalentServices;
import com.steerify.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TalentServicesImpl implements TalentServices {

    private final TalentRepository talentRepository;
    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;
    private final JwtUserRepository jwtUserRepository;
    private final AdminRepository adminRepository;


    public TalentDto signUp(TalentDto talentDto) {
        if (talentDto.getPassword() == null || talentDto.getPassword().isBlank()) {
            throw new IllegalArgumentException("Password cannot be null or empty");
        }

        if( !talentRepository.existsByEmail(talentDto.getEmail()) ) {
            Talent talent = TalentMapper.mapDtoToTalent(talentDto);
            talent.setRole(Role.TALENT);
            talent.setPassword(passwordEncoder.encode(talentDto.getPassword()));
            talent.setTalentId(UUID.randomUUID());
            Talent savedTalent = talentRepository.save(talent);
            jwtUserRepository.save(savedTalent);
            return TalentMapper.mapTalentToDto(savedTalent);
        }
        throw new ResourceNotFoundException("Client with email: "+talentDto.getEmail()+" already exists");

    }


    public LoginResponseDto signIn(LoginRequestDto request) {
         return authService.loginUser(request);
    }

    public List<ClientDto> findByCompanyName(String nameOfCompany) {
        List<Client> foundClients = clientRepository.findByCompanyNameEqualsIgnoreCase(nameOfCompany);
        return foundClients.stream()
                .map(ClientMapper::mapToCLientDto)
                .collect(Collectors.toList());
    }
}


