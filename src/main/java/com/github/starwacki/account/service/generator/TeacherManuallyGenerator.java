package com.github.starwacki.account.service.generator;


import com.github.starwacki.account.dto.AccountStudentDTO;
import com.github.starwacki.account.dto.AccountTeacherDTO;
import com.github.starwacki.account.model.Role;
import com.github.starwacki.account.model.Student;
import com.github.starwacki.account.model.Teacher;
import com.github.starwacki.repository.SchoolClassRepository;
import com.github.starwacki.repository.StudentRepository;
import com.github.starwacki.repository.TeacherRepository;
import org.springframework.stereotype.Service;

@Service
public class TeacherManuallyGenerator  extends AccountGenerator{


    public TeacherManuallyGenerator(StudentRepository studentRepository, SchoolClassRepository schoolClassRepository, TeacherRepository teacherRepository) {
        super(studentRepository, schoolClassRepository, teacherRepository);
    }

    public Teacher generateTeacherAccount(AccountTeacherDTO accountTeacherDTO) {
        return Teacher.builder()
                .firstname(accountTeacherDTO.firstname())
                .lastname(accountTeacherDTO.lastname())
                .role(Role.TEACHER)
                .subject(accountTeacherDTO.subject())
                .username(generateAccountUsername(accountTeacherDTO.firstname(),accountTeacherDTO.lastname(),getLastTeacherId()))
                .password(generateFirstPassword())
                .workPhone(accountTeacherDTO.workPhone())
                .email(accountTeacherDTO.email())
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
