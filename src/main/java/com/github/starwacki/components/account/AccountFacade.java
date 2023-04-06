package com.github.starwacki.components.account;

import com.github.starwacki.common.password_encoder.EgradePasswordEncoder;
import com.github.starwacki.components.account.dto.AccountStudentDTO;
import com.github.starwacki.components.account.dto.AccountTeacherDTO;
import com.github.starwacki.components.account.exceptions.IllegalOperationException;
import com.github.starwacki.components.account.exceptions.WrongPasswordException;
import com.github.starwacki.components.account.dto.AccountViewDTO;
import com.github.starwacki.components.account.exceptions.AccountNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import java.util.List;

@AllArgsConstructor
@Service
public class AccountFacade {

    private final AccountFactory accountFactory;
    private final AccountStudentRepository accountStudentRepository;
    private final AccountTeacherRepository accountTeacherRepository;
    private final AccountParentRepository accountParentRepository;
    private final EgradePasswordEncoder egradePasswordEncoder;

    public List<AccountViewDTO> saveStudentsAndParentsFromFile(String path) {
      return   accountFactory.createStudents(path)
              .stream()
              .map(accountStudentDTO -> saveStudentAndParentAccount(accountStudentDTO))
              .toList();
    }
    public AccountViewDTO saveStudentAndParentAccount(AccountStudentDTO studentDTO) {
        AccountStudent accountStudent = accountFactory.createStudent(studentDTO);
        AccountParent accountParent = accountFactory.createParent(studentDTO);
        accountStudent.setAccountParent(accountParent);
        accountStudentRepository.save(accountStudent);
        return AccountMapper.mapAccountToAccountViewDTO(egradePasswordEncoder,accountStudent);
    }

    public AccountViewDTO changeAccountPassword(AccountRole accountRole, int id, String oldPassword, String newPassword) {
        AccountViewDTO accountViewDTO;
        switch (accountRole) {
            case STUDENT -> accountViewDTO = changePassword(accountStudentRepository,id,oldPassword,newPassword);
            case TEACHER -> accountViewDTO = changePassword(accountTeacherRepository,id,oldPassword,newPassword);
            case PARENT -> accountViewDTO = changePassword(accountParentRepository,id,oldPassword,newPassword);
            default -> throw new IllegalOperationException(HttpMethod.PUT, accountRole.toString());
        }
        return accountViewDTO;
    }

    public AccountViewDTO getAccountById(AccountRole accountRole, int id) {
        AccountViewDTO accountViewDTO;
        switch (accountRole) {
            case STUDENT -> accountViewDTO = getAccount(accountStudentRepository,id);
            case TEACHER -> accountViewDTO = getAccount(accountTeacherRepository,id);
            case PARENT -> accountViewDTO = getAccount(accountParentRepository,id);
            default -> throw new IllegalOperationException(HttpMethod.GET, accountRole.toString());
        }
        return accountViewDTO;
    }

    public AccountViewDTO deleteAccountById(AccountRole accountRole, int id) {
        AccountViewDTO accountViewDTO;
        switch (accountRole) {
            case STUDENT -> accountViewDTO = deleteAccount(accountStudentRepository,id);
            case TEACHER -> accountViewDTO = deleteAccount(accountTeacherRepository,id);
            default -> throw new IllegalOperationException(HttpMethod.DELETE, accountRole.toString());
        }
        return accountViewDTO;
    }

    public AccountViewDTO saveTeacherAccount(AccountTeacherDTO accountTeacherDTO) {
        AccountTeacher accountTeacher = accountTeacherRepository.save(accountFactory.createTeacher(accountTeacherDTO));
        return AccountMapper.mapAccountToAccountViewDTO(egradePasswordEncoder,accountTeacher);
    }

    private <T extends Account> AccountViewDTO getAccount(JpaRepository<T,Integer> jpaRepository, int id) {
        return jpaRepository
                .findById(id)
                .map(account -> AccountMapper.mapAccountToAccountViewDTO(egradePasswordEncoder,account))
                .orElseThrow(() -> new AccountNotFoundException(id));
    }

    private  <T extends Account> AccountViewDTO changePassword(JpaRepository<T,Integer> jpaRepository, int id, String oldPassword, String newPassword) {
        return jpaRepository
                .findById(id)
                .map(account -> setNewPassword(jpaRepository,account,oldPassword,newPassword))
                .map(account -> AccountMapper.mapAccountToAccountViewDTO(egradePasswordEncoder,account))
                .orElseThrow(()-> new AccountNotFoundException(id));
    }

    private  <T extends Account> T setNewPassword(JpaRepository<T,Integer> jpaRepository,T account,String oldPassword, String newPassword) {
        if (arePasswordsSame(account,oldPassword))
            return setPassword(jpaRepository,account,newPassword);
        else
            throw new WrongPasswordException();
    }

    private <T extends  Account> T setPassword(JpaRepository<T,Integer> repository, T account, String newPassword) {
        account.getAccountDetails().setPassword(egradePasswordEncoder.encode(newPassword));
        return  repository.save(account);
    }

    private boolean arePasswordsSame(Account account, String oldPassword) {
        return egradePasswordEncoder.encode(oldPassword).equals(account.getAccountDetails().getPassword());
    }

    private <T extends Account> AccountViewDTO deleteAccount(JpaRepository<T,Integer> repository,int id) {
            T account = repository
                    .findById(id).
                    orElseThrow(() -> new AccountNotFoundException(id));
            repository.delete(account);
            return AccountMapper.mapAccountToAccountViewDTO(egradePasswordEncoder,account);
    }
}
