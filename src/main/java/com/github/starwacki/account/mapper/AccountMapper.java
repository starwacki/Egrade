package com.github.starwacki.account.mapper;

import com.github.starwacki.account.dto.AccountViewDTO;
import com.github.starwacki.account.model.Account;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {

   public static AccountViewDTO mapAccountToAccountViewDTO(Account account) {
       return AccountViewDTO
               .builder()
               .id(account.getId())
               .firstname(account.getFirstname())
               .lastname(account.getLastname())
               .username(account.getUsername())
               .password(account.getPassword())
               .accountType(account.getRole().toString())
               .build();
   }



}
