package com.github.starwacki.account.service;

import com.github.starwacki.account.exception.WrongPasswordException;
import com.github.starwacki.account.mapper.AccountMapper;
import com.github.starwacki.account.model.Parent;
import com.github.starwacki.account.model.Student;
import com.github.starwacki.model.SchoolClass;
import com.github.starwacki.repository.SchoolClassRepository;
import com.github.starwacki.repository.StudentRepository;
import com.github.starwacki.account.dto.AccountStudentDTO;
import com.github.starwacki.account.dto.AccountViewDTO;
import com.github.starwacki.account.exception.WrongFileException;
import com.github.starwacki.account.service.generator.ParentManuallyGenerator;
import com.github.starwacki.account.service.generator.StudentCSVGenerator;
import com.github.starwacki.account.service.generator.StudentManuallyGenerator;
import com.github.starwacki.service.student_grade_service.exceptions.StudentNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
public class AccountService {

    private final StudentManuallyGenerator studentManuallyGenerator;

    private final ParentManuallyGenerator parentManuallyGenerator;

    private final StudentCSVGenerator studentCSVGenerator;

    private final StudentRepository studentRepository;

    private final SchoolClassRepository schoolClassRepository;


       /*
          Method generate two account: generate account for student and account for student's parent,
          parent account is assigned to specified student.
          first password  is randomly generated.
         */

    public List<Student> saveStudentsAndParentsFromFile(String path) throws WrongFileException, IOException {
      return   studentCSVGenerator
              .generateStudents(path)
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


    public List<AccountViewDTO> getAllStudentsFromClass(String className, int classYear) {
        return studentRepository.findAllBySchoolClassNameAndSchoolClassClassYear(className,classYear)
                .stream()
                .map(student ->AccountMapper.mapStudentToAccountViewDTO(student))
                .toList();
    }

    public AccountViewDTO getStudentAccount(int id) {
        return studentRepository.findById(id)
                .map(student -> AccountMapper.mapStudentToAccountViewDTO(student))
                .orElseThrow(() -> new StudentNotFoundException());
    }

    public AccountViewDTO changeStudentPassword(int id, String oldPassword, String newPassword) {
       return studentRepository.findById(id)
               .map(student -> setNewPassword(student,oldPassword,newPassword))
               .map(student -> AccountMapper.mapStudentToAccountViewDTO(student))
               .orElseThrow(() -> new StudentNotFoundException());
    }

    public AccountViewDTO deleteStudentAccountById(int id) {
        if (isStudentExist(id))
            return deleteStudentAccount(id);
        else
            throw new StudentNotFoundException();
    }

    private AccountViewDTO deleteStudentAccount(int id) {
        Student student = studentRepository.findStudentById(id);
        studentRepository.delete(student);
        return AccountMapper.mapStudentToAccountViewDTO(student);
    }

    private boolean isStudentExist(int id) {
        return studentRepository.findById(id).isPresent();
    }


    public AccountViewDTO changeStudentClass(int id, String className, int year) {
        return studentRepository.findById(id)
                .map(student -> setSchoolClass(student,className,year))
                .map(student -> AccountMapper.mapStudentToAccountViewDTO(student))
                .orElseThrow(() -> new StudentNotFoundException());
    }


    private Student setNewPassword(Student student, String oldPassword,String newPassword) {
        if (arePasswordsSame(student,oldPassword))
            return studentWithChangedPassword(student,newPassword);
        else
            throw new WrongPasswordException();
    }

    private boolean arePasswordsSame(Student student,String oldPassword) {
        return student.getPassword().equals(oldPassword);
    }

    private Student studentWithChangedPassword(Student student, String newPassword) {
        student.setPassword(newPassword);
        studentRepository.save(student);
        return student;
    }

    private Student setSchoolClass(Student student, String className, int year) {
        student.setSchoolClass(getSchoolClass(className,year));
        studentRepository.save(student);
        return student;
    }

    private SchoolClass getSchoolClass(String className, int year) {
        return schoolClassRepository
                .findByNameAndClassYear(className,year)
                .orElse(createNewSchoolClass(className,year));
    }

    private SchoolClass createNewSchoolClass(String className,int year) {
        return new SchoolClass(className,year);
    }

}
