package com.github.starwacki.account.service;

import com.github.starwacki.account.dto.AccountTeacherDTO;
import com.github.starwacki.account.exception.WrongPasswordException;
import com.github.starwacki.account.mapper.AccountMapper;
import com.github.starwacki.account.model.*;
import com.github.starwacki.account.service.generator.TeacherManuallyGenerator;
import com.github.starwacki.model.SchoolClass;
import com.github.starwacki.repository.SchoolClassRepository;
import com.github.starwacki.repository.StudentRepository;
import com.github.starwacki.account.dto.AccountStudentDTO;
import com.github.starwacki.account.dto.AccountViewDTO;
import com.github.starwacki.account.exception.WrongFileException;
import com.github.starwacki.account.service.generator.ParentManuallyGenerator;
import com.github.starwacki.account.service.generator.StudentCSVGenerator;
import com.github.starwacki.account.service.generator.StudentManuallyGenerator;
import com.github.starwacki.repository.TeacherRepository;
import com.github.starwacki.account.exception.AccountNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
public class AccountService {

    private final StudentManuallyGenerator studentManuallyGenerator;
    private final ParentManuallyGenerator parentManuallyGenerator;

    private final TeacherManuallyGenerator teacherManuallyGenerator;
    private final StudentCSVGenerator studentCSVGenerator;
    private final StudentRepository studentRepository;
    private final SchoolClassRepository schoolClassRepository;
    private final TeacherRepository teacherRepository;


       /*
          Method generate two account: generate account for student and account for student's parent,
          parent account is assigned to specified student.
          first password  is randomly generated.
         */

    public List<AccountViewDTO> saveStudentsAndParentsFromFile(String path) throws WrongFileException, IOException {
      return   studentCSVGenerator
              .generateStudents(path)
              .stream()
              .map(accountStudentDTO -> saveStudentAndParentAccount(accountStudentDTO))
              .toList();
    }
    public AccountViewDTO saveStudentAndParentAccount(AccountStudentDTO studentDTO) {
        Student student = studentManuallyGenerator.generateStudentAccount(studentDTO);
        Parent parent = parentManuallyGenerator.generateParentAccount(studentDTO);
        student.setParent(parent);
        studentRepository.save(student);
        return AccountMapper.mapAccountToAccountViewDTO(student);
    }


    public List<AccountViewDTO> getAllStudentsFromClass(String className, int classYear) {
        return studentRepository.findAllBySchoolClassNameAndSchoolClassClassYear(className,classYear)
                .stream()
                .map(student ->AccountMapper.mapAccountToAccountViewDTO(student))
                .toList();
    }

    public AccountViewDTO changeAccountPassword(Role role,int id, String oldPassword,String newPassword) {
        AccountViewDTO accountViewDTO;
        switch (role) {
            case STUDENT -> accountViewDTO = changePassword(studentRepository,id,oldPassword,newPassword);
            case TEACHER -> accountViewDTO = changePassword(teacherRepository,id,oldPassword,newPassword);
            default -> throw new IllegalStateException("Unexpected value: " + role);
        }
        return accountViewDTO;
    }

    public AccountViewDTO getAccountById(Role role, int id) {
        AccountViewDTO accountViewDTO;
        switch (role) {
            case STUDENT -> accountViewDTO = getAccount(studentRepository,id);
            case TEACHER -> accountViewDTO = getAccount(teacherRepository,id);
            default -> throw new IllegalStateException("Unexpected value: " + role);
        }
        return accountViewDTO;
    }

    private <T extends Account> AccountViewDTO getAccount(JpaRepository<T,Integer> jpaRepository, int id) {
        return jpaRepository.findById(id)
                .map(t -> AccountMapper.mapAccountToAccountViewDTO(t))
                .orElseThrow(() -> new AccountNotFoundException());
    }

    private  <T extends Account> AccountViewDTO changePassword(JpaRepository<T,Integer> jpaRepository, int id, String oldPassword, String newPassword) {
        return jpaRepository.findById(id)
                .map(account -> setNewPassword(jpaRepository,account,oldPassword,newPassword))
                .map(account -> AccountMapper.mapAccountToAccountViewDTO(account))
                .orElseThrow(()-> new AccountNotFoundException());
    }

    private  <T extends Account> T setNewPassword(JpaRepository<T,Integer> jpaRepository,T account,String oldPassword, String newPassword) {
        if (arePasswordsSame(account,oldPassword)) {
            account.setPassword(newPassword);
            jpaRepository.save(account);
        } else
            throw new WrongPasswordException();
        return account;
    }

    private boolean arePasswordsSame(Account account, String oldPassword) {
        return account.getPassword().equals(oldPassword);
    }

    public AccountViewDTO deleteStudentAccountById(int id) {
        if (isStudentExist(id))
            return deleteStudentAccount(id);
        else
            throw new AccountNotFoundException();
    }

    public AccountViewDTO changeStudentClass(int id, String className, int year) {
        return studentRepository.findById(id)
                .map(student -> setSchoolClass(student,className,year))
                .map(student -> AccountMapper.mapAccountToAccountViewDTO(student))
                .orElseThrow(() -> new AccountNotFoundException());
    }

    public AccountViewDTO saveTeacherAccount(AccountTeacherDTO accountTeacherDTO) {
        Teacher teacher = teacherRepository.save(teacherManuallyGenerator.generateTeacherAccount(accountTeacherDTO));
        return AccountMapper.mapAccountToAccountViewDTO(teacher);
    }

    private AccountViewDTO deleteStudentAccount(int id) {
        Student student = studentRepository.findStudentById(id);
        studentRepository.delete(student);
        return AccountMapper.mapAccountToAccountViewDTO(student);
    }

    private boolean isStudentExist(int id) {
        return studentRepository.findById(id).isPresent();
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
