package com.github.starwacki.common.model.account;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "details")
class AccountDetails {

    @Id
    private String username;

    private String password;

    private String role;

    private String createdDate;
}
