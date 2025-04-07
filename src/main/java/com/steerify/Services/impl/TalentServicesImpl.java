package com.steerify.Services.impl;


import com.steerify.Dtos.ClientDto;
import com.steerify.Dtos.TalentDto;
import com.steerify.Entities.Client;
import com.steerify.Entities.Talent;
import com.steerify.Enums.Role;
import com.steerify.Helpers.LoginRequestDto;
import com.steerify.Helpers.LoginResponseDto;
import com.steerify.Helpers.auth.AuthService;
import com.steerify.Helpers.auth.exception.UserNotFoundException;
import com.steerify.Mappers.ClientMapper;
import com.steerify.Repositories.AdminRepository;
import com.steerify.Repositories.ClientRepository;
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
        List<Client> foundClients = clientRepository.findByCompanyNameContainingIgnoreCase(nameOfCompany);
        return foundClients.stream()
                .map(ClientMapper::mapToCLientDto)
                .collect(Collectors.toList());
    }

    @Override
    public TalentDto findById(UUID talentId) {
        Talent talent = talentRepository.findById(talentId).orElseThrow(() -> new ResourceNotFoundException("Talent with id: "+talentId+" not found"));
        return TalentMapper.mapTalentToDto(talent);
    }

    @Override
    public TalentDto updateTalent(UUID talentId, TalentDto talentDto) {
        Talent existingTalent = talentRepository.findById(talentId).orElseThrow(()-> new UserNotFoundException("Talent Not Found"));
        existingTalent.setFirstName(talentDto.getFirstName());
        existingTalent.setLastName(talentDto.getLastName());
        existingTalent.setEmail(talentDto.getEmail());
        existingTalent.setPhoneNumber(talentDto.getPhoneNumber());
        existingTalent.setAddress(talentDto.getAddress());
        existingTalent.setEducation(talentDto.getEducation());
        existingTalent.setGender(talentDto.getGender());
        existingTalent.setSkills(talentDto.getSkills());

        if (talentDto.getPassword() != null && !talentDto.getPassword().isEmpty()) {
            existingTalent.setPassword(passwordEncoder.encode(talentDto.getPassword()));
        }
        talentRepository.save(existingTalent);

        return TalentMapper.mapTalentToDto(existingTalent);
    }

    @Override
    public void deleteTalent(UUID talentId) {
        Talent talent = TalentMapper.mapDtoToTalent(findById(talentId));
        talentRepository.delete(talent);

    }
}


