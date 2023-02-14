package com.github.starwacki.account.mapper;


import com.github.starwacki.account.dto.AccountViewDTO;
import com.github.starwacki.account.model.Student;
import org.springframework.stereotype.Component;

@Component()
public class AccountMapper {

   public static AccountViewDTO mapStudentToAccountViewDTO(Student student) {
       return AccountViewDTO
               .builder()
               .id(student.getId())
               .firstname(student.getFirstname())
               .lastname(student.getLastname())
               .username(student.getUsername())
               .password(student.getPassword())
               .accountType(student.getRole().toString())
               .build();
   }

//   public static AccountViewDTO mapTeacherToAccountViewDTO(Teach)
}
