package com.github.starwacki.account.service.generator;

import com.github.starwacki.repositories.SchoolClassRepository;
import com.github.starwacki.repositories.StudentRepository;
import com.github.starwacki.account.dto.AccountStudentDTO;
import com.github.starwacki.account.exceptions.WrongFileException;
import com.github.starwacki.repositories.TeacherRepository;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.springframework.stereotype.Service;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class StudentCSVGenerator extends StudentManuallyGenerator {

    private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();


    protected StudentCSVGenerator(StudentRepository studentRepository, SchoolClassRepository schoolClassRepository, TeacherRepository teacherRepository) {
        super(studentRepository, schoolClassRepository, teacherRepository);
    }

    public List<AccountStudentDTO> generateStudents(String path)  {
        File file  = new File(path);
        if (isCsvFile(file) && !isFileEmpty(file)) {
            try {
                return  getStudentFromFileTest(file);
            } catch (IOException e) {
                throw new WrongFileException(e.getMessage());
            }
        } else
            throw new WrongFileException();
    }

    private boolean isFileEmpty(File file) {
        if (file.length()==0)
            throw new WrongFileException("File is empty");
        else
            return false;
    }

    private boolean isCsvFile(File file) {
        return file.exists() && file.isFile() && file.getName().endsWith(".csv");
    }

    private List<AccountStudentDTO> getStudentFromFileTest(File file) throws IOException {
        AtomicInteger atomicInteger = new AtomicInteger();
        return Files.readAllLines(Path.of(file.getPath()))
                .stream()
                .skip(1)
                .map(s -> getAccountStudentDTOFromLine(s.split(","),atomicInteger.getAndIncrement()))
                .toList();

    }

    private AccountStudentDTO getAccountStudentDTOFromLine(String[] line, int lineIndex) {
        if (isLineProperty(line))
            return getValidatedAccountStudentDTO(line,lineIndex);
        else
            throw new WrongFileException("Line is empty or not have call, line number; " + (lineIndex+1));
    }

    private AccountStudentDTO getValidatedAccountStudentDTO(String[] line, int lineIndex) {
        try {
            AccountStudentDTO student = map(line);
            validateLine(student,lineIndex);
            return student;
        } catch (NumberFormatException e) {
            throw new WrongFileException("Wrong year format, line number: "+ (lineIndex+1));
        }
    }

    private void validateLine(AccountStudentDTO student, int lineIndex) {
        if (!validator.validate(student).isEmpty()) {
            throw new WrongFileException("Incorrect data format, line number: " + (lineIndex + 1));
        }
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
