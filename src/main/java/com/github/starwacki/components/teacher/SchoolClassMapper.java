package com.github.starwacki.components.teacher;

import com.github.starwacki.components.teacher.SchoolClassDTO;
import com.github.starwacki.common.model.school_class.SchoolClass;

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
