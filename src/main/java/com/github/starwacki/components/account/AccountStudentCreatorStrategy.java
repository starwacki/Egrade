package com.github.starwacki.components.account;

import com.github.starwacki.components.account.dto.AccountStudentDTO;
import com.github.starwacki.common.model.school_class.SchoolClass;
import com.github.starwacki.common.repositories.SchoolClassRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
class StudentManuallyGeneratorStrategy extends AccountGeneratorStrategy {


    public StudentManuallyGeneratorStrategy(StudentRepository studentRepository,
                                            SchoolClassRepository schoolClassRepository,
                                            TeacherRepository teacherRepository) {
        super(studentRepository, schoolClassRepository, teacherRepository);
    }

    @Override
    public Student createAccount(Record dto) {
        AccountStudentDTO studentDTO = (AccountStudentDTO) dto;
        return Student.builder()
                .firstname(studentDTO.firstname())
                .lastname(studentDTO.lastname())
                .schoolClass(getSchoolClass(studentDTO))
                .accountDetails(getAccountDetails(studentDTO))
                .build();
    }

    private AccountDetails getAccountDetails(AccountStudentDTO studentDTO) {
        return AccountDetails
                .builder()
                .username(generateAccountUsername(studentDTO.firstname(),studentDTO.lastname(),getLastStudentId()))
                .password(generateFirstPassword())
                .createdDate(LocalDate.now().toString())
                .role(Role.STUDENT)
                .build();
    }

    private SchoolClass getSchoolClass(AccountStudentDTO studentDTO) {
        return schoolClassRepository.findSchoolClassByNameAndAndClassYear(studentDTO.className(), studentDTO.year())
                .orElse(getNewSchoolClass(studentDTO));

    }

    private long getLastStudentId() {
        return (studentRepository.count() + 1);
    }

    private SchoolClass getNewSchoolClass(AccountStudentDTO studentDTO) {
        return new SchoolClass(studentDTO.className(), studentDTO.year());
    }

    @Override
    protected String generateAccountUsername(String firstname, String lastname, long id) {
        return firstname + "." + lastname + getStudentAccountIdentity()+id;
    }

    private String getStudentAccountIdentity() {
        return "STU";
    }
}
