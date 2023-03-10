package com.github.starwacki.components.account.service;

import com.github.starwacki.components.account.dto.AccountStudentDTO;
import com.github.starwacki.components.account.dto.AccountTeacherDTO;
import com.github.starwacki.components.account.exceptions.IllegalOperationException;
import com.github.starwacki.components.account.exceptions.WrongPasswordException;
import com.github.starwacki.components.account.service.generator.ParentManuallyGeneratorStrategy;
import com.github.starwacki.components.account.service.generator.StudentManuallyGeneratorStrategy;
import com.github.starwacki.components.account.service.generator.TeacherManuallyGeneratorStrategy;
import com.github.starwacki.components.account.mapper.AccountMapper;
import com.github.starwacki.components.account.dto.AccountViewDTO;
import com.github.starwacki.components.account.exceptions.AccountNotFoundException;
import com.github.starwacki.components.account.service.generator.StudentCSVGeneratorStrategy;
import com.github.starwacki.global.model.account.*;
import com.github.starwacki.global.repositories.ParentRepository;
import com.github.starwacki.global.repositories.StudentRepository;
import com.github.starwacki.global.repositories.TeacherRepository;
import com.github.starwacki.global.security.AES;
import com.github.starwacki.global.security.EgradePasswordEncoder;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpMethod;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;

@AllArgsConstructor
@Service
public class AccountService {

    private final StudentManuallyGeneratorStrategy studentManuallyGeneratorStrategy;
    private final ParentManuallyGeneratorStrategy parentManuallyGeneratorStrategy;
    private final TeacherManuallyGeneratorStrategy teacherManuallyGeneratorStrategy;
    private final StudentCSVGeneratorStrategy studentCSVGeneratorStrategy;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final ParentRepository parentRepository;

    public List<AccountViewDTO> saveStudentsAndParentsFromFile(String path) {
      return   studentCSVGeneratorStrategy
              .generateStudents(path)
              .stream()
              .map(accountStudentDTO -> saveStudentAndParentAccount(accountStudentDTO))
              .toList();
    }
    public AccountViewDTO saveStudentAndParentAccount(AccountStudentDTO studentDTO) {
        Student student = studentManuallyGeneratorStrategy.createAccount(studentDTO);
        Parent parent = parentManuallyGeneratorStrategy.createAccount(studentDTO);
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
        Teacher teacher = teacherRepository.save(teacherManuallyGeneratorStrategy.createAccount(accountTeacherDTO));
        return AccountMapper.mapAccountToAccountViewDTO(teacher);
    }

    private <T extends Account> AccountViewDTO getAccount(JpaRepository<T,Integer> jpaRepository, int id) {
        return jpaRepository
                .findById(id)
                .map(account -> AccountMapper.mapAccountToAccountViewDTO(account))
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
        account.setPassword(AES.encrypt(newPassword));
        return  repository.save(account);
    }

    private boolean arePasswordsSame(Account account, String oldPassword) {
        return AES.encrypt(oldPassword).equals(account.getPassword());
    }

    private <T extends Account> AccountViewDTO deleteAccount(JpaRepository<T,Integer> repository,int id) {
            T account = repository
                    .findById(id).
                    orElseThrow(() -> new AccountNotFoundException(id));
            repository.delete(account);
            return AccountMapper.mapAccountToAccountViewDTO(account);
    }
}
