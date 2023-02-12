package com.github.starwacki.service.account_generator_service;

import com.github.starwacki.model.account.Parent;
import com.github.starwacki.model.account.Student;
import com.github.starwacki.repository.StudentRepository;
import com.github.starwacki.service.account_generator_service.dto.AccountStudentDTO;
import com.github.starwacki.service.account_generator_service.exception.WrongFileException;
import com.github.starwacki.service.account_generator_service.generator.ParentManuallyGenerator;
import com.github.starwacki.service.account_generator_service.generator.StudentCSVGenerator;
import com.github.starwacki.service.account_generator_service.generator.StudentManuallyGenerator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
public class AccountGeneratorService {

    private final StudentManuallyGenerator studentManuallyGenerator;

    private final ParentManuallyGenerator parentManuallyGenerator;

    private final StudentCSVGenerator studentCSVGenerator;

    private final StudentRepository studentRepository;


       /*
          Method generate two account: generate account for student and account for student's parent,
          parent account is assigned to specified student.
          first password  is randomly generated.
         */


    public List<Student> saveStudentsAndParentsFromFile(String path) throws WrongFileException, IOException {
      return   studentCSVGenerator.generateStudents(path)
              .stream()
              .map(accountStudentDTO -> saveStudentAndParentAccount(accountStudentDTO))
              .toList();
    }
    public Student saveStudentAndParentAccount(AccountStudentDTO studentDTO) {
        Student student = studentManuallyGenerator.generateStudentAccount(studentDTO);
        Parent parent = parentManuallyGenerator.generateParentAccount(studentDTO);
        student.setParent(parent);
        studentRepository.save(student);
        return student;
    }



}
