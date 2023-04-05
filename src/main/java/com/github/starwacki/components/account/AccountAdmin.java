package com.github.starwacki.components.account;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "admins")
class Admin extends Account{

    protected Admin() {
    }

}
