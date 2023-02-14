package com.github.starwacki.account.service.generator;


import com.github.starwacki.repository.SchoolClassRepository;
import com.github.starwacki.repository.StudentRepository;
import org.springframework.stereotype.Service;

@Service
public class TeacherManuallyGenerator  extends AccountGenerator{


    protected TeacherManuallyGenerator(StudentRepository studentRepository, SchoolClassRepository schoolClassRepository) {
        super(studentRepository, schoolClassRepository);
    }


}
