package com.github.starwacki.components.account;

import com.github.starwacki.components.auth.EgradePasswordEncoder;
import com.github.starwacki.components.account.dto.AccountResponseDTO;
import org.springframework.stereotype.Component;

@Component
class AccountMapper {

    private AccountMapper() {
    }


   public static AccountResponseDTO mapAccountToAccountViewDTO(EgradePasswordEncoder passwordEncoder, Account account) {
       return AccountResponseDTO
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
