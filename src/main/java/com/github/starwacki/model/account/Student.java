package com.github.starwacki.model.account;


import com.github.starwacki.model.SchoolClass;
import com.github.starwacki.service.student_service.StudentDTO;
import jakarta.persistence.*;
import lombok.*;

@Data
@Entity
@Table(name = "students")
public class Student extends Account {

    private int diaryNumber;

    @ManyToOne
    @JoinColumn(name = "classes_id")
    private SchoolClass schoolClass;

    @OneToOne
    private Parent parent;

    protected Student() {
    }

    private Student(String username, String password, String firstname, String lastname, Role role, int diaryNumber, SchoolClass schoolClass, Parent parent) {
        super(username, password, firstname, lastname, role);
        this.diaryNumber = diaryNumber;
        this.schoolClass = schoolClass;
        this.parent = parent;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder extends  Account.Builder<Student> {

        private int diaryNumber;

        private SchoolClass schoolClass;

        private Parent parent;

        public Builder diaryNumber(int diaryNumber) {
            this.diaryNumber = diaryNumber;
            return this;
        }

        public Builder schoolClass(SchoolClass schoolClass) {
            this.schoolClass= schoolClass;
            return this;
        }

        public Builder parent(Parent parent) {
            this.parent = parent;
            return this;
        }

        @Override
        public Student build() {
            return null;
        }
    }

}
