package com.github.starwacki.components.account;

import com.github.starwacki.components.account.dto.AccountStudentRequestDTO;
import com.github.starwacki.components.account.dto.AccountTeacherRequestDTO;
import com.github.starwacki.components.account.dto.AccountResponseDTO;
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
    public ResponseEntity<AccountResponseDTO> addStudent(AccountStudentRequestDTO studentDTO) {
        AccountResponseDTO student = accountFacade.saveStudentAndParentAccount(studentDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(student);

    }

    @Secured(value = {"ADMIN"})
    @PostMapping("/students")
    public ResponseEntity<List<AccountResponseDTO>> addStudentsFromCSVFile(String pathname) {
      List<AccountResponseDTO> list = accountFacade.saveStudentsAndParentsFromFile(pathname);
      return ResponseEntity.status(HttpStatus.CREATED).body(list);
    }

    @Secured(value = {"ADMIN"})
    @PostMapping("/teacher")
    public ResponseEntity<AccountResponseDTO> addTeacher(AccountTeacherRequestDTO accountTeacherRequestDTO) {
        AccountResponseDTO teacher = accountFacade.saveTeacherAccount(accountTeacherRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(teacher);
    }

    @Secured(value = {"ADMIN"})
    @GetMapping("/{accountRole}={id}")
    public ResponseEntity<AccountResponseDTO> getAccountById(
            @PathVariable AccountRole accountRole,
            @PathVariable int id) {
        AccountResponseDTO accountResponseDTO = accountFacade.getAccountById(accountRole,id);
        return ResponseEntity.ok(accountResponseDTO);
    }

    @Secured(value = {"ADMIN"})
    @DeleteMapping("/{accountRole}={id}")
    public ResponseEntity<AccountResponseDTO> deleteAccountById(
            @PathVariable AccountRole accountRole,
            @PathVariable int id) {
        AccountResponseDTO accountResponseDTO = accountFacade.deleteAccountById(accountRole,id);
        return ResponseEntity.ok(accountResponseDTO);
    }


    //Todo - implement jwt token
    @PermitAll
    @PutMapping("password/{accountRole}={id}")
    public ResponseEntity<AccountResponseDTO> changeAccountPassword(
            @PathVariable AccountRole accountRole,
            @PathVariable int id,
            @RequestParam String oldPassword,
            @RequestParam @Pattern(regexp = "^(?=.*\\d)(?=.*[A-Z])(?=.*\\W).{6,25}$") String newPassword) {
        AccountResponseDTO accountResponseDTO = accountFacade.changeAccountPassword(accountRole,id,oldPassword,newPassword);
        return ResponseEntity.ok(accountResponseDTO);
    }



}
