package com.github.starwacki.components.account.mapper;

import com.github.starwacki.components.account.dto.AccountViewDTO;
import com.github.starwacki.global.model.account.Account;
import org.springframework.stereotype.Component;

@Component
public class AccountMapper {

    private AccountMapper() {
    }

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
