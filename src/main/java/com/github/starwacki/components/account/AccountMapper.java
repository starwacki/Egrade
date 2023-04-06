package com.github.starwacki.components.account;

import com.github.starwacki.common.password_encoder.EgradePasswordEncoder;
import com.github.starwacki.components.account.dto.AccountViewDTO;
import org.springframework.stereotype.Component;

@Component
class AccountMapper {

    private AccountMapper() {
    }


   public static AccountViewDTO mapAccountToAccountViewDTO(EgradePasswordEncoder passwordEncoder,Account account) {
       return AccountViewDTO
               .builder()
               .id(account.getId())
               .firstname(account.getFirstname())
               .lastname(account.getLastname())
               .username(account.getAccountDetails().getUsername())
               .password(passwordEncoder.decode(account.getAccountDetails().getPassword()))
               .accountType(account.getAccountDetails().getAccountRole().toString())
               .build();
   }



}
