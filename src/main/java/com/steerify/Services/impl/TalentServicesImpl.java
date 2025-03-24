package com.steerify.Services.impl;


import com.steerify.Dtos.ClientDto;
import com.steerify.Dtos.TalentDto;
import com.steerify.Entities.Client;
import com.steerify.Entities.Talent;
import com.steerify.Enums.Role;
import com.steerify.Helpers.LoginRequestDto;
import com.steerify.Helpers.LoginResponseDto;
import com.steerify.Helpers.auth.AuthService;
import com.steerify.Mappers.ClientMapper;
import com.steerify.Repositories.ClientRepository;
import com.steerify.Enums.TalentEnum;
import com.steerify.Mappers.TalentMapper;
import com.steerify.Repositories.TalentRepository;
import com.steerify.Services.TalentServices;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TalentServicesImpl implements TalentServices {

    private final TalentRepository talentRepository;
    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthService authService;




    public TalentDto signUp(TalentDto talentDto) {
        Talent talent = TalentMapper.mapDtoToTalent(talentDto);
        talent.setRole(Role.TALENT);
        talent.setAvailability(TalentEnum.OFFLINE);

        talent.setPassword(passwordEncoder.encode(talent.getPassword()));

        talentRepository.save(talent);
        return TalentMapper.mapTalentToDto(talent);
    }


    public LoginResponseDto signIn(LoginRequestDto request) {
        Talent talent = talentRepository.findByEmailAddress(request.getEmail())
                .orElseThrow(()-> new IllegalArgumentException("Talent not found"));
        if (!passwordEncoder.matches(
                request.getPassword(), talent.getPassword())) {
            throw new IllegalArgumentException("Wrong password");
        }
        return authService.loginUser(request);
    }

    public List<ClientDto> findByCompanyName(String nameOfCompany) {
        List<Client> foundClients = clientRepository.findByCompanyNameEqualsIgnoreCase(nameOfCompany);
        return foundClients.stream()
                .map(ClientMapper::mapToCLientDto)
                .collect(Collectors.toList());
    }
}


