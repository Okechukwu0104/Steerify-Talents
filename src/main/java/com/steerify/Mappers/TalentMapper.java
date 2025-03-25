package com.steerify.Mappers;

import com.steerify.Dtos.TalentDto;
import com.steerify.Entities.Talent;
import com.steerify.Enums.Role;

import java.util.UUID;

public class TalentMapper {
     public static TalentDto mapTalentToDto(Talent talent) {
         return new TalentDto(
                 talent.getTalentId(),
                 talent.getFirstName(),
                 talent.getLastName(),
                 talent.getAge(),
                 talent.getGender(),
                 talent.getEmail(),
                 talent.getPhoneNumber(),
                 talent.getAddress(),
                 talent.getSkills(),
                 talent.getEducation(),
                 talent.getAvailability()

         );
     }


     public static Talent mapDtoToTalent(TalentDto talentDto) {
         return new Talent(
                 talentDto.getTalentId() != null ? talentDto.getTalentId() : UUID.randomUUID(),
                 talentDto.getFirstName(),
                 talentDto.getLastName(),
                 talentDto.getAge(),
                 talentDto.getGender(),
                 talentDto.getEmail(),
                 talentDto.getPhoneNumber(),
                 talentDto.getAddress(),
                 talentDto.getSkills(),
                 talentDto.getEducation(),
                 talentDto.getAvailability(),
                 talentDto.getPassword(),
                 Role.TALENT

         );
     }
}
