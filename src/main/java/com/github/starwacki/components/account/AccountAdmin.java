package com.github.starwacki.components.account;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Table(name = "admins")
class AccountAdmin extends Account{

    protected AccountAdmin() {
    }

}
