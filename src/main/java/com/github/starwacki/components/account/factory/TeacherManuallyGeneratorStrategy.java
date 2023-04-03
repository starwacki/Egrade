package com.github.starwacki.components.account.factory;


import com.github.starwacki.components.account.dto.AccountTeacherDTO;
import com.github.starwacki.common.model.account.Role;
import com.github.starwacki.common.model.account.Teacher;
import com.github.starwacki.common.repositories.SchoolClassRepository;
import com.github.starwacki.common.repositories.StudentRepository;
import com.github.starwacki.common.repositories.TeacherRepository;
import org.springframework.stereotype.Service;

@Service
class TeacherManuallyGeneratorStrategy extends AccountGeneratorStrategy {


    public TeacherManuallyGeneratorStrategy(StudentRepository studentRepository,
                                            SchoolClassRepository schoolClassRepository,
                                            TeacherRepository teacherRepository) {
        super(studentRepository, schoolClassRepository, teacherRepository);
    }

    @Override
    public Teacher createAccount(Record dto) {
        AccountTeacherDTO teacherDTO = (AccountTeacherDTO) dto;
        return Teacher.builder()
                .firstname(teacherDTO.firstname())
                .lastname(teacherDTO.lastname())
                .role(Role.TEACHER)
                .subject(teacherDTO.subject())
                .username(generateAccountUsername(teacherDTO.firstname(),teacherDTO.lastname(),getLastTeacherId()))
                .password(generateFirstPassword())
                .workPhone(teacherDTO.workPhone())
                .email(teacherDTO.email())
                .build();
    }

    private long getLastTeacherId() {
       return teacherRepository.count() +1;
    }

    @Override
    protected String generateAccountUsername(String firstname, String lastname, long id) {
        return firstname +"." + lastname + "NAU"+ getLastTeacherId();
    }


}
