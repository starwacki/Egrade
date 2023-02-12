package com.github.starwacki.service.account_generator_service.generator;

import com.github.starwacki.repository.SchoolClassRepository;
import com.github.starwacki.repository.StudentRepository;
import com.github.starwacki.service.account_generator_service.dto.AccountStudentDTO;
import com.github.starwacki.service.account_generator_service.exception.WrongFileException;
import org.springframework.stereotype.Service;
import java.io.*;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

@Service
public class StudentCSVGenerator extends AccountGenerator {


    public StudentCSVGenerator(StudentRepository studentRepository, SchoolClassRepository schoolClassRepository) {
        super(studentRepository, schoolClassRepository);
    }



    public List<AccountStudentDTO> generateStudents(String path) throws WrongFileException, IOException {
        File file  = new File(path);
        if (isCsvFile(file)) {
           return  getStudentFromFile(file);
        } else
            throw new WrongFileException();
    }

    private boolean isCsvFile(File file) {
        return file.exists() && file.isFile() && file.getName().endsWith(".csv");
    }

    private List<AccountStudentDTO> getStudentFromFile(File file) throws IOException, WrongFileException {
        List<AccountStudentDTO>  students = new LinkedList<>();
        BufferedReader fileReader = new BufferedReader(new FileReader(file));
        String line = fileReader.readLine();
        while (line!=null) {
            line = fileReader.readLine();
            if (line != null) {
                String[] lineTable = line.split(",");
                if (isLineProperty(lineTable)) {
                    AccountStudentDTO student = map(lineTable);
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

    private AccountStudentDTO map(String[] line) {
        return  AccountStudentDTO.builder()
                .firstname(line[0])
                .lastname(line[1])
                .year(Integer.parseInt(line[2]))
                .className(line[3])
                .parentPhoneNumber(line[4])
                .build();
    }




}
