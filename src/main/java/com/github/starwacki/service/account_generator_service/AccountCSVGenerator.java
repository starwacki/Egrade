package com.github.starwacki.service.account_generator_service;

import com.github.starwacki.model.account.Student;
import com.github.starwacki.repository.SchoolClassRepository;
import com.github.starwacki.repository.StudentRepository;
import com.github.starwacki.service.account_generator_service.exception.WrongFileException;
import org.springframework.stereotype.Service;
import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Service
public class AccountCSVGenerator extends AccountGenerator {


    public AccountCSVGenerator(StudentRepository studentRepository, SchoolClassRepository schoolClassRepository) {
        super(studentRepository, schoolClassRepository);
    }



    public List<Student> generateStudents(File file) throws WrongFileException, IOException {
        if (isCsvFile(file)) {
           List<Student> students = getStudentFromFile(file);
           studentRepository.saveAll(students);
           return students;
        } else
            throw new WrongFileException();
    }

    private boolean isCsvFile(File file) {
        return file.exists() && file.isFile() && file.getName().endsWith(".csv");
    }

    private List<Student> getStudentFromFile(File file) throws IOException, WrongFileException {
        List<Student>  students = new LinkedList<>();
        BufferedReader fileReader = new BufferedReader(new FileReader(file));
        String line = fileReader.readLine();
        while (line!=null) {
            line = fileReader.readLine();
            if (line != null) {
                String[] lineTable = line.split(",");
                if (isLineProperty(lineTable)) {
                    Student student = generateStudentAndParentAccount(map(lineTable));
                    students.add(student);
                } else throw new WrongFileException();
            }
        }
        return students;
    }

    private boolean isLineProperty(String[] line) {
        return line.length == 5 && Arrays.stream(line)
                .noneMatch(field -> field == null);
    }

    private StudentDTO map(String[] line) {
        return  StudentDTO.builder()
                .firstname(line[0])
                .lastname(line[1])
                .year(Integer.parseInt(line[2]))
                .className(line[3])
                .parentPhoneNumber(line[4])
                .build();
    }




}
