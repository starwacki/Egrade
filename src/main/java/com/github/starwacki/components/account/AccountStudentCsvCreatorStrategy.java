package com.github.starwacki.components.account;

import com.github.starwacki.components.auth.EgradePasswordEncoder;
import com.github.starwacki.components.account.dto.AccountStudentRequestDTO;
import com.github.starwacki.components.account.exceptions.WrongFileException;
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
class AccountStudentCsvCreatorStrategy extends AccountStudentCreatorStrategy {

    private static final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();


    public AccountStudentCsvCreatorStrategy(AccountStudentRepository accountStudentRepository,
                                            AccountTeacherRepository accountTeacherRepository,
                                            EgradePasswordEncoder egradePasswordEncoder) {
        super(accountStudentRepository, accountTeacherRepository, egradePasswordEncoder);
    }

    public List<AccountStudentRequestDTO> generateStudents(String path)  {
        File file  = new File(path);
        if (isCsvFile(file) && !isFileEmpty(file))
            return generateStudentFormFile(file);
         else
            throw new WrongFileException(WrongFileException.Code.FILE);
    }

    private List<AccountStudentRequestDTO> generateStudentFormFile(File file) {
        try {
            return getStudentFromFileTest(file);
        } catch (IOException e) {
            throw new WrongFileException(e.getMessage());
        }
    }

    private boolean isFileEmpty(File file) {
        return file.length()==0;

    }

    private boolean isCsvFile(File file) {
        return file.exists() && file.isFile() && file.getName().endsWith(".csv");

    }

    private List<AccountStudentRequestDTO> getStudentFromFileTest(File file) throws IOException {
        AtomicInteger atomicInteger = new AtomicInteger();
        return Files.readAllLines(Path.of(file.getPath()))
                .stream()
                .skip(1)
                .map(s -> getAccountStudentDTOFromLine(s.split(","),atomicInteger.getAndIncrement()))
                .toList();

    }

    private AccountStudentRequestDTO getAccountStudentDTOFromLine(String[] line, int lineIndex) {
        if (isLineProperty(line))
            return getValidatedAccountStudentDTO(line,lineIndex);
        else
            throw new WrongFileException(WrongFileException.Code.LINE, getLineIndexInFile(lineIndex));
    }

    private int getLineIndexInFile(int lineIndex) {
        return lineIndex+1;
    }

    private AccountStudentRequestDTO getValidatedAccountStudentDTO(String[] line, int lineIndex) {
        try {
            AccountStudentRequestDTO student = map(line);
            validateLine(student,lineIndex);
            return student;
        } catch (NumberFormatException e) {
            throw new WrongFileException(WrongFileException.Code.YEAR_FORMAT,getLineIndexInFile(lineIndex));
        }
    }

    private void validateLine(AccountStudentRequestDTO student, int lineIndex) {
        if (!validator.validate(student).isEmpty()) {
            throw new WrongFileException(WrongFileException.Code.VALIDATION,getLineIndexInFile(lineIndex));
        }
    }

    private boolean isLineProperty(String[] line) {
        return line.length == 5 && Arrays.stream(line)
                .noneMatch(field -> field == null);
    }

    private AccountStudentRequestDTO map(String[] line) {
        return  AccountStudentRequestDTO.builder()
                .firstname(line[0])
                .lastname(line[1])
                .year(Integer.parseInt(line[2]))
                .className(line[3])
                .parentPhoneNumber(line[4])
                .build();
    }




}
