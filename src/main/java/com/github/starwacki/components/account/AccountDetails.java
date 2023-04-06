package com.github.starwacki.components.account;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "details")
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
class AccountDetails {


    @Id
    private String username;

    @Setter
    private String password;

    @Enumerated(EnumType.STRING)
    private AccountRole accountRole;

    private String createdDate;
}
