package com.github.starwacki.components.account;

import com.github.starwacki.components.account.dto.AccountViewDTO;
import com.github.starwacki.common.model.account.Account;
import com.github.starwacki.common.security.AES;
import org.springframework.stereotype.Component;

@Component
class AccountMapper {

    private AccountMapper() {
    }

   public static AccountViewDTO mapAccountToAccountViewDTO(Account account) {
       return AccountViewDTO
               .builder()
               .id(account.getId())
               .firstname(account.getFirstname())
               .lastname(account.getLastname())
               .username(account.getUsername())
               .password(AES.decrypt(account.getPassword()))
               .accountType(account.getRole().toString())
               .build();
   }



}
