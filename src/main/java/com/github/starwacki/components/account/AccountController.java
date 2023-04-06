package com.github.starwacki.components.account;

import com.github.starwacki.components.account.dto.AccountStudentDTO;
import com.github.starwacki.components.account.dto.AccountTeacherDTO;
import com.github.starwacki.components.account.dto.AccountViewDTO;
import jakarta.annotation.security.PermitAll;
import jakarta.validation.constraints.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RequiredArgsConstructor
@Validated
@Controller
@RequestMapping("/account")
class AccountController implements AccountControllerOperations {

    private final AccountFacade accountFacade;

    @Secured(value = {"ADMIN"})
    @PostMapping("/student")
    public ResponseEntity<AccountViewDTO> addStudent(AccountStudentDTO studentDTO) {
        AccountViewDTO student = accountFacade.saveStudentAndParentAccount(studentDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(student);

    }

    @Secured(value = {"ADMIN"})
    @PostMapping("/students")
    public ResponseEntity<List<AccountViewDTO>> addStudentsFromCSVFile(String pathname) {
      List<AccountViewDTO> list = accountFacade.saveStudentsAndParentsFromFile(pathname);
      return ResponseEntity.status(HttpStatus.CREATED).body(list);
    }

    @Secured(value = {"ADMIN"})
    @PostMapping("/teacher")
    public ResponseEntity<AccountViewDTO> addTeacher(AccountTeacherDTO accountTeacherDTO) {
        AccountViewDTO teacher = accountFacade.saveTeacherAccount(accountTeacherDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(teacher);
    }

    @Secured(value = {"ADMIN"})
    @GetMapping("/{accountRole}={id}")
    public ResponseEntity<AccountViewDTO> getAccountById(
            @PathVariable AccountRole accountRole,
            @PathVariable int id) {
        AccountViewDTO accountViewDTO = accountFacade.getAccountById(accountRole,id);
        return ResponseEntity.ok(accountViewDTO);
    }

    @Secured(value = {"ADMIN"})
    @DeleteMapping("/{accountRole}={id}")
    public ResponseEntity<AccountViewDTO> deleteAccountById(
            @PathVariable AccountRole accountRole,
            @PathVariable int id) {
        AccountViewDTO accountViewDTO = accountFacade.deleteAccountById(accountRole,id);
        return ResponseEntity.ok(accountViewDTO);
    }


    //Todo - implement jwt token
    @PermitAll
    @PutMapping("password/{accountRole}={id}")
    public ResponseEntity<AccountViewDTO> changeAccountPassword(
            @PathVariable AccountRole accountRole,
            @PathVariable int id,
            @RequestParam String oldPassword,
            @RequestParam @Pattern(regexp = "^(?=.*\\d)(?=.*[A-Z])(?=.*\\W).{6,25}$") String newPassword) {
        AccountViewDTO accountViewDTO = accountFacade.changeAccountPassword(accountRole,id,oldPassword,newPassword);
        return ResponseEntity.ok(accountViewDTO);
    }



}
