package com.steerify.Mappers.Reusables;

import com.steerify.Dtos.Reusables.ApplicationDto;
import com.steerify.Entities.Reusables.Application;

public class ApplicationMapper {

    public static ApplicationDto mapToApplDto(Application application) {
        return new ApplicationDto(
                application.getApplicationId(),
                application.getTalentId(),
                application.getJobId(),
                application.getCoverLetter(),
                application.getStats()
        );
    }

    public static Application mapToAppl(ApplicationDto applicationDto){
        return new Application(
                applicationDto.getApplicationId(),
                applicationDto.getTalentId(),
                applicationDto.getJobId(),
                applicationDto.getCoverLetter(),
                applicationDto.getStats()
        );
    }
}
