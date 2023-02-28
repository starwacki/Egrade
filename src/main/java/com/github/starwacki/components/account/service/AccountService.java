package com.github.starwacki.components.account.service;

import com.github.starwacki.components.account.dto.AccountStudentDTO;
import com.github.starwacki.components.account.dto.AccountTeacherDTO;
import com.github.starwacki.components.account.exceptions.exception.IllegalOperationException;
import com.github.starwacki.components.account.exceptions.exception.WrongPasswordException;
import com.github.starwacki.components.account.model.*;
import com.github.starwacki.components.account.service.generator.ParentManuallyGenerator;
import com.github.starwacki.components.account.service.generator.StudentManuallyGenerator;
import com.github.starwacki.components.account.service.generator.TeacherManuallyGenerator;
import com.github.starwacki.components.account.mapper.AccountMapper;
import com.github.starwacki.components.account.dto.AccountViewDTO;
import com.github.starwacki.components.account.exceptions.exception.AccountNotFoundException;
import com.github.starwacki.components.account.exceptions.exception.WrongFileException;
import com.github.starwacki.components.account.service.generator.StudentCSVGenerator;
import com.github.starwacki.global.repositories.ParentRepository;
import com.github.starwacki.global.repositories.StudentRepository;
import com.github.starwacki.global.repositories.TeacherRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import java.util.List;

@AllArgsConstructor
@Service
public class AccountService {

    private final StudentManuallyGenerator studentManuallyGenerator;
    private final ParentManuallyGenerator parentManuallyGenerator;
    private final TeacherManuallyGenerator teacherManuallyGenerator;
    private final StudentCSVGenerator studentCSVGenerator;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final ParentRepository parentRepository;

    public List<AccountViewDTO> saveStudentsAndParentsFromFile(String path) {
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

    public AccountViewDTO changeAccountPassword(Role role, int id, String oldPassword, String newPassword) {
        AccountViewDTO accountViewDTO;
        switch (role) {
            case STUDENT -> accountViewDTO = changePassword(studentRepository,id,oldPassword,newPassword);
            case TEACHER -> accountViewDTO = changePassword(teacherRepository,id,oldPassword,newPassword);
            case PARENT -> accountViewDTO = changePassword(parentRepository,id,oldPassword,newPassword);
            default -> throw new IllegalOperationException(HttpMethod.PUT,role);
        }
        return accountViewDTO;
    }

    public AccountViewDTO getAccountById(Role role, int id) {
        AccountViewDTO accountViewDTO;
        switch (role) {
            case STUDENT -> accountViewDTO = getAccount(studentRepository,id);
            case TEACHER -> accountViewDTO = getAccount(teacherRepository,id);
            case PARENT -> accountViewDTO = getAccount(parentRepository,id);
            default -> throw new IllegalOperationException(HttpMethod.GET,role);
        }
        return accountViewDTO;
    }

    public AccountViewDTO deleteAccountById(Role role,int id) {
        AccountViewDTO accountViewDTO;
        switch (role) {
            case STUDENT -> accountViewDTO = deleteAccount(studentRepository,id);
            case TEACHER -> accountViewDTO = deleteAccount(teacherRepository,id);
            default -> throw new IllegalOperationException(HttpMethod.DELETE,role);
        }
        return accountViewDTO;
    }

    public AccountViewDTO saveTeacherAccount(AccountTeacherDTO accountTeacherDTO) {
        Teacher teacher = teacherRepository.save(teacherManuallyGenerator.generateTeacherAccount(accountTeacherDTO));
        return AccountMapper.mapAccountToAccountViewDTO(teacher);
    }

    private <T extends Account> AccountViewDTO getAccount(JpaRepository<T,Integer> jpaRepository, int id) {
        return jpaRepository
                .findById(id)
                .map(t -> AccountMapper.mapAccountToAccountViewDTO(t))
                .orElseThrow(() -> new AccountNotFoundException(id));
    }

    private  <T extends Account> AccountViewDTO changePassword(JpaRepository<T,Integer> jpaRepository, int id, String oldPassword, String newPassword) {
        return jpaRepository
                .findById(id)
                .map(account -> setNewPassword(jpaRepository,account,oldPassword,newPassword))
                .map(account -> AccountMapper.mapAccountToAccountViewDTO(account))
                .orElseThrow(()-> new AccountNotFoundException(id));
    }

    private  <T extends Account> T setNewPassword(JpaRepository<T,Integer> jpaRepository,T account,String oldPassword, String newPassword) {
        if (arePasswordsSame(account,oldPassword))
            return setPassword(jpaRepository,account,newPassword);
        else
            throw new WrongPasswordException();
    }

    private <T extends  Account> T setPassword(JpaRepository<T,Integer> repository, T account, String newPassword) {
        account.setPassword(newPassword);
        return  repository.save(account);
    }

    private boolean arePasswordsSame(Account account, String oldPassword) {
        return account.getPassword().equals(oldPassword);
    }

    private <T extends Account> AccountViewDTO deleteAccount(JpaRepository<T,Integer> repository,int id) {
            T account = repository
                    .findById(id).
                    orElseThrow(() -> new AccountNotFoundException(id));
            repository.delete(account);
            return AccountMapper.mapAccountToAccountViewDTO(account);


    }
}
