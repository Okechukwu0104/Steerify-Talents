package com.steerify.Mappers;

import com.steerify.Dtos.TalentDto;
import com.steerify.Entities.Talent;

public class TalentMapper {
     public static TalentDto mapTalentToDto(Talent talent) {
         return new TalentDto(
                 talent.getTalentId(),
                 talent.getFirstName(),
                 talent.getLastName(),
                 talent.getEmailAddress(),
                 talent.getPhoneNumber(),
                 talent.getAddress(),
                 talent.getSkills(),
                 talent.getEducation(),
                 talent.getAvailability()

         );
     }


     public static Talent mapDtoToTalent(TalentDto talentDto) {
         return new Talent(
                 talentDto.getTalentId(),
                 talentDto.getFirstName(),
                 talentDto.getLastName(),
                 talentDto.getAge(),
                 talentDto.getGender(),
                 talentDto.getEmailAddress(),
                 talentDto.getPhoneNumber(),
                 talentDto.getAddress(),
                 talentDto.getSkills(),
                 talentDto.getEducation(),
                 talentDto.getAvailability(),
                 talentDto.getPassword()

         );
     }
}
