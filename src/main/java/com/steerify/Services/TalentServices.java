package com.steerify.Services;


import com.steerify.Dtos.ClientDto;
import com.steerify.Dtos.TalentDto;

import com.steerify.Helpers.LoginRequestDto;
import com.steerify.Helpers.LoginResponseDto;


import java.util.List;
import java.util.UUID;


public interface TalentServices {
    TalentDto signUp(TalentDto talentDto) ;
    LoginResponseDto signIn(LoginRequestDto request);
    List<ClientDto> findByCompanyName(String nameOfCompany);
    TalentDto findById(UUID talentId);
    TalentDto updateTalent(UUID talentId, TalentDto talentDto);
    void deleteTalent(UUID talentId);
}


