package com.github.starwacki.components.teacher.mapper;

import com.github.starwacki.components.teacher.dto.SchoolClassDTO;
import com.github.starwacki.global.model.school_class.SchoolClass;

public class SchoolClassMapper {

    private SchoolClassMapper() {

    }

    public static SchoolClassDTO mapSchoolClassToSchoolClassDTO(SchoolClass schoolClass) {
        return SchoolClassDTO.builder()
                .className(schoolClass.getName())
                .year(schoolClass.getClassYear())
                .build();
    }


}
