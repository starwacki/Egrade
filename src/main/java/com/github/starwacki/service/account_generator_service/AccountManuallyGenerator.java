package com.github.starwacki.service.account_generator_service;

import com.github.starwacki.model.account.Student;
import com.github.starwacki.repository.SchoolClassRepository;
import com.github.starwacki.repository.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccountManuallyGenerator extends AccountGenerator {


    public AccountManuallyGenerator(StudentRepository studentRepository, SchoolClassRepository schoolClassRepository) {
        super(studentRepository, schoolClassRepository);
    }

    public Optional<Student> getStudent(int id) {
        return studentRepository.findById(id);
    }



}
