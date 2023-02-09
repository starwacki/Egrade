package com.github.starwacki.model.account;


import com.github.starwacki.model.SchoolClass;
import jakarta.persistence.*;
import lombok.*;
import java.util.Set;

@Data
@Entity
@Table(name = "teachers")
public class Teacher extends Account {

    private String workPhone;

    private String email;

    protected Teacher() {

    }

    private Teacher(String username, String password, String firstname, String lastname, Role role, String workPhone, String email) {
        super(username, password, firstname, lastname, role);
        this.workPhone = workPhone;
        this.email = email;
    }

    @ManyToMany
    @JoinTable(
            name = "teachers_classes",
            joinColumns = @JoinColumn(name = "teachers_id"),
            inverseJoinColumns = @JoinColumn(name = "classes_id")
    )
    private Set<SchoolClass> classes;

    public static class Builder extends Account.Builder<Teacher> {

        private String workPhone;

        private String email;

        public Builder workPhone(String workPhone) {
            this.workPhone = workPhone;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }
        @Override
        public Teacher build() {
            return new Teacher(username,password,firstname,lastname,role,workPhone,email);
        }
    }



}
