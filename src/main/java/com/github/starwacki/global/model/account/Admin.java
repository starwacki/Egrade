package com.github.starwacki.global.model.account;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "admins")
public class Admin extends Account{

    protected Admin() {
    }

}
