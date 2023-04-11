package com.github.starwacki.components.schoolclass;

import com.github.starwacki.components.teacher.dto.TeacherSchoolClassDTO;

class SchoolClassMapper {

    private SchoolClassMapper() {

    }

    public static TeacherSchoolClassDTO mapSchoolClassToSchoolClassDTO(SchoolClass schoolClass) {
        return TeacherSchoolClassDTO.builder()
                .className(schoolClass.getName())
                .year(schoolClass.getClassYear())
                .build();
    }


}
