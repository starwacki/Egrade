package com.github.starwacki.service.account_generator_service;

import com.github.starwacki.model.SchoolClass;
import com.github.starwacki.model.account.Parent;
import com.github.starwacki.model.account.Role;
import com.github.starwacki.model.account.Student;
import com.github.starwacki.repository.SchoolClassRepository;
import com.github.starwacki.repository.StudentRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;


@Service
@AllArgsConstructor
public abstract class AccountGenerator {

    protected final StudentRepository studentRepository;

    protected final SchoolClassRepository schoolClassRepository;

       /*
          Method generate two account: generate account for student and account for student's parent,
          parent account is assigned to specified student.
          first password  is randomly generated.
         */
    public Student generateStudentAndParentAccount(StudentDTO studentDTO) {
        Student student = generateStudentAccount(studentDTO);
        Parent parent = generateParentAccount(studentDTO);
        student.setParent(parent);
        studentRepository.save(student);
        return student;
    }

    private Student generateStudentAccount(StudentDTO studentDTO) {
        return Student.builder()
                .firstname(studentDTO.firstname())
                .lastname(studentDTO.lastname())
                .role(Role.STUDENT)
                .schoolClass(getSchoolClass(studentDTO))
                .username(generateUsername(studentDTO))
                .password(generateFirstPassword())
                .build();
    }

    private String generateUsername(StudentDTO studentDTO) {
        return studentDTO.firstname() + "." + studentDTO.lastname() + (studentRepository.count() + 1);
    }

    private String generateFirstPassword() {
        StringBuilder stringBuilder = new StringBuilder();
        addRandomNumber(stringBuilder);
        return stringBuilder.toString();
    }

    private void addRandomNumber(StringBuilder stringBuilder) {
        Random random = new Random();
        int randomNumber;
        for (int i = 0; i < 10; i++) {
            randomNumber = random.nextInt(65, 123);
            if (isCharLetter(randomNumber)) {
                randomNumber += 10;
            }
            stringBuilder.append((char) (randomNumber));
        }
    }

    private boolean isCharLetter(int randomNumber) {
        return randomNumber >= 91 && randomNumber <= 96;
    }

    private Parent generateParentAccount(StudentDTO studentDTO) {
        return Parent.builder()
                .firstname(studentDTO.firstname())
                .lastname(studentDTO.lastname())
                .username(generateParentUsername(studentDTO))
                .password(generateFirstPassword())
                .role(Role.PARENT)
                .phoneNumber(studentDTO.parentPhoneNumber())
                .build();
    }

    private String generateParentUsername(StudentDTO studentDTO) {
        return studentDTO.firstname() + "." + studentDTO.lastname() + getLastId() + getParentAccountIdentity();
    }

    private String getParentAccountIdentity() {
        return "RO";
    }

    private long getLastId() {
        return (studentRepository.count() + 1);
    }

    private SchoolClass getSchoolClass(StudentDTO studentDTO) {
        return schoolClassRepository.findByNameAndClassYear(studentDTO.className(), studentDTO.year())
                .orElse(getNewSchoolClass(studentDTO));

    }
    private SchoolClass getNewSchoolClass(StudentDTO studentDTO) {
        return new SchoolClass(studentDTO.className(), studentDTO.year());
    }


}
