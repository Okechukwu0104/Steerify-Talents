package com.steerify.Services;


import com.steerify.Dtos.ClientDto;
import com.steerify.Dtos.TalentDto;

import com.steerify.Helpers.LoginRequestDto;
import com.steerify.Helpers.LoginResponseDto;


import java.util.List;


public interface TalentServices {
    TalentDto signUp(TalentDto talentDto) ;
    LoginResponseDto signIn(LoginRequestDto request);
    List<ClientDto> findByCompanyName(String nameOfCompany);
}


