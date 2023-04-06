package com.github.starwacki.components.account;

import com.github.starwacki.components.account.dto.AccountStudentDTO;
import com.github.starwacki.components.account.exceptions.WrongFileException;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class AccountStudentCsvCreatorUnitTest {

    @InjectMocks
    private AccountStudentCsvCreatorStrategy accountStudentCsvCreatorStrategy;

    private final static String NO_EXIST_FILE_PATH  = "no_exist_file.csv";
    private final static String FILE_WITHOUT_CSV_END_PATH = "src/test/resources/csv_generator_test/file_without_csv_end.txt";
    private final static String DIRECTORY_NOT_FILE_PATH = "src/test/resources/csv_generator_test/not_file.csv";
    private final static String EMPTY_FILE_PATH = "src/test/resources/csv_generator_test/empty_file.csv";
    private final static String FILE_WITH_GOOD_DATA = "src/test/resources/csv_generator_test/test_adding_student.csv";

    private void prepareData(String filePath, String ... data) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter(filePath,false));
        writer.write("Imie,Nazwisko,Rok,Nazwa klasy,Numer do rodzica" + "\n");
        for(String line : data) {
            writer.write(line + "\n");
        }
        writer.close();
    }

    @BeforeAll
    public static void prepareFiles() throws IOException {
         new File(NO_EXIST_FILE_PATH).createNewFile();
         new File(FILE_WITHOUT_CSV_END_PATH).createNewFile();
         new File(DIRECTORY_NOT_FILE_PATH).mkdir();
         new File(EMPTY_FILE_PATH).createNewFile();
         new File(FILE_WITH_GOOD_DATA).createNewFile();
    }

    @AfterAll
    public static void deleteFiles() {
        new File(NO_EXIST_FILE_PATH).delete();
        new File(FILE_WITHOUT_CSV_END_PATH).delete();
        new File(DIRECTORY_NOT_FILE_PATH).delete();
        new File(EMPTY_FILE_PATH).delete();
        new File(FILE_WITH_GOOD_DATA).delete();
    }

    @Test
    @DisplayName("Test adding student from csv file when file no exist")
    void generateStudents_givenNoExistFilePath_shouldThrowWrongFileException() {
        //given
        String filePath = NO_EXIST_FILE_PATH;

        //then
        assertThrows(WrongFileException.class,() -> accountStudentCsvCreatorStrategy.generateStudents(filePath));
    }

    @Test
    @DisplayName("Test adding student from csv file when file not end with .csv format")
    void generateStudents_givenFileWithoutCSVEnd_shouldThrowWrongFileException() {
        //given
        String filePath = FILE_WITHOUT_CSV_END_PATH;

        //then
        assertThrows(WrongFileException.class,() -> accountStudentCsvCreatorStrategy.generateStudents(filePath));
    }

    @Test
    @DisplayName("Test adding student from csv file when not give file")
    void generateStudents_givenDirectory_shouldThrowWrongFileException() {
        //given
        String filePath = DIRECTORY_NOT_FILE_PATH;

        //then
        assertThrows(WrongFileException.class,() -> accountStudentCsvCreatorStrategy.generateStudents(filePath));
    }

    @Test
    @DisplayName("Test adding student from csv file when file is empty")
    void generateStudents_givenEmptyCsvFile_shouldThrowWrongFileException() {
        //given
        String filePath = EMPTY_FILE_PATH;

        //then
        assertThrows(WrongFileException.class,() -> accountStudentCsvCreatorStrategy.generateStudents(filePath));
    }

    @Test
    @DisplayName("Test adding student from csv file when file is empty return correct exception message")
    void generateStudents_givenEmptyCsvFile_shouldThrowWrongFileExceptionWithEmptyInformationMessage() {
        //given
        String filePath = EMPTY_FILE_PATH;

        //when
        Exception exception = assertThrows(WrongFileException.class, () -> accountStudentCsvCreatorStrategy.generateStudents(filePath));
        String actualMessage = exception.getMessage();

        //then
        String expectedMessage = "File is empty or file can't be read";
        assertEquals(actualMessage, expectedMessage);

    }

    @Test
    @DisplayName("Test adding student from csv file when file has good data")
    void generateStudents_givenFileWith4Rows_shouldReturn4SizeList() throws IOException {
        //given
        String filePath = FILE_WITH_GOOD_DATA;
        prepareData(filePath,
                "firstname,lastname,2020,2A,111222333",
                      "firstname,lastname,2020,2A,111222333",
                      "firstname,lastname,2020,2A,111222333",
                      "firstname,lastname,2020,2A,111222333"
                );

        //when
        List<AccountStudentDTO> accountStudentDTOList = accountStudentCsvCreatorStrategy.generateStudents(filePath);


        //then
        assertThat(accountStudentDTOList,hasSize(4));
    }

    @Test
    @DisplayName("Test adding student from csv file and returning correct AccountStudentDTO")
    void generateStudents_givenFileWith4Rows_shouldReturn4AccountDtoWithSameFieldsLikeGivenData() throws IOException {
        //given
        String filePath = FILE_WITH_GOOD_DATA;
        prepareData(filePath,
                "Lukasz,Szwacz,2021,1A,123456790",
                "Bozena,Krewetka,2024,2A,987654321",
                "Wojciech,Mazur,2023,1A,111222333",
                "Dave,Szuwarek,2022,2A,123453339"
        );

        //when
        List<AccountStudentDTO> accountStudentDTOList = accountStudentCsvCreatorStrategy.generateStudents(filePath);
        AccountStudentDTO expectedAccountDtoWithLastIndex = AccountStudentDTO.builder()
                .firstname("Dave")
                .lastname("Szuwarek")
                .year(2022)
                .className("2A")
                .parentPhoneNumber("123453339")
                .build();

        //then
        assertThat(accountStudentDTOList.get(3),equalTo(expectedAccountDtoWithLastIndex));
    }

    @Test
    @DisplayName("Test adding student from csv file when one cell is null")
    void generateStudents_givenFileWithOneNullCell_shouldThrowWrongFileException() throws IOException {
        //given
        String filePath = FILE_WITH_GOOD_DATA;
        prepareData(filePath,
                "Lukasz,,2021,1A,123456790",
                "Bozena,Krewetka,2024,2A,987654321",
                "Wojciech,Mazur,2023,1A,111222333",
                "Dave,Szuwarek,2022,2A,123453339"
        );

        //then
        assertThrows(WrongFileException.class,() -> accountStudentCsvCreatorStrategy.generateStudents(filePath));
    }

    @Test
    @DisplayName("Test adding student from csv file when one cell is null return correct exception message")
    void generateStudents_givenFileWithOneNullCell_shouldBeValidated_andReturnValidatedExceptionMessage() throws IOException {
        //given
        String filePath = FILE_WITH_GOOD_DATA;
        prepareData(filePath,
                "Lukasz,,2021,1A,123456790",
                "Bozena,Krewetka,2024,2A,987654321",
                "Wojciech,Mazur,2023,1A,111222333",
                "Dave,Szuwarek,2022,2A,123453339"
        );

        //when
        Exception exception = assertThrows(WrongFileException.class, () -> accountStudentCsvCreatorStrategy.generateStudents(filePath));
        String actualMessage = exception.getMessage();

        //then
        String expectedMessage = "Incorrect data format, line number: 1";
        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    @DisplayName("Test adding student from csv file when one row is too long")
    void generateStudents_givenFileWithTooLongLine_shouldThrowWrongFileException() throws IOException {
        //given
        String filePath = FILE_WITH_GOOD_DATA;
        prepareData(filePath,
                "Lukasz,Szwacz,2021,1A,123456790,test",
                "Bozena,Krewetka,2024,2A,987654321",
                "Wojciech,Mazur,2023,1A,111222333",
                "Dave,Szuwarek,2022,2A,123453339"
        );

        //then
        assertThrows(WrongFileException.class,() -> accountStudentCsvCreatorStrategy.generateStudents(filePath));
    }

    @Test
    @DisplayName("Test adding student from csv file when one row is too long return correct exception message")
    void generateStudents_givenFileWithTooLongLine_shouldThrowWrongFileExceptionWithIncorrectLineMessage() throws IOException {
        //given
        String filePath = FILE_WITH_GOOD_DATA;
        prepareData(filePath,
                "Lukasz,Szwacz,2021,1A,123456790,test",
                "Bozena,Krewetka,2024,2A,987654321",
                "Wojciech,Mazur,2023,1A,111222333",
                "Dave,Szuwarek,2022,2A,123453339"
        );

        //when
        Exception exception = assertThrows(WrongFileException.class, () -> accountStudentCsvCreatorStrategy.generateStudents(filePath));
        String actualMessage = exception.getMessage();

        //then
        String expectedMessage ="Line is empty or not have call, line number: 1";
        assertEquals(actualMessage, expectedMessage);
    }

    @Test
    @DisplayName("Test adding student from csv file when firstname has illegal characters")
    void generateStudents_givenFileWithIllegalFirstnameCharacters_shouldThrowWrongFileException() throws IOException {
        //given
        String filePath = FILE_WITH_GOOD_DATA;
        prepareData(filePath,
                "Lukas$,Szwacz,2021,1A,123456790",
                "Bozena,Krewetka,2024,2A,987654321",
                "Wojciech,Mazur,2023,1A,111222333",
                "Dave,Szuwarek,2022,2A,123453339"
        );

        //then
        assertThrows(WrongFileException.class,() -> accountStudentCsvCreatorStrategy.generateStudents(filePath));
    }

    @Test
    @DisplayName("Test adding student from csv file when lastname has illegal characters")
    void generateStudents_givenFileWithIllegalLastnameCharacters_shouldThrowWrongFileException() throws IOException {
        //given
        String filePath = FILE_WITH_GOOD_DATA;
        prepareData(filePath,
                "Lukasz,Sz*33,2021,1A,123456790",
                "Bozena,Krewetka,2024,2A,987654321",
                "Wojciech,Mazur,2023,1A,111222333",
                "Dave,Szuwarek,2022,2A,123453339"
        );

        //then
        assertThrows(WrongFileException.class,() -> accountStudentCsvCreatorStrategy.generateStudents(filePath));
    }

    @Test
    @DisplayName("Test adding student from csv file when year is String")
    void generateStudents_givenFileWithYearAsString_shouldThrowWrongFileException() throws IOException {
        //given
        String filePath = FILE_WITH_GOOD_DATA;
        prepareData(filePath,
                "Lukasz,Szwacz,202S1,1A,123456790",
                "Bozena,Krewetka,2024,2A,987654321",
                "Wojciech,Mazur,2023,1A,111222333",
                "Dave,Szuwarek,2022,2A,123453339"
        );

        //then
        assertThrows(WrongFileException.class,() -> accountStudentCsvCreatorStrategy.generateStudents(filePath));
    }

    @Test
    @DisplayName("Test adding student from csv file when year is String")
    void generateStudents_givenFileWithYearAsString_shouldThrowWrongFileExceptionWithWrongYearMessage() throws IOException {
        //given
        String filePath = FILE_WITH_GOOD_DATA;
        prepareData(filePath,
                "Lukasz,Szwacz,202S1,1A,123456790",
                "Bozena,Krewetka,2024,2A,987654321",
                "Wojciech,Mazur,2023,1A,111222333",
                "Dave,Szuwarek,2022,2A,123453339"
        );

        //when
        Exception exception = assertThrows(WrongFileException.class, () -> accountStudentCsvCreatorStrategy.generateStudents(filePath));
        String actualMessage = exception.getMessage();

        //then
        String expectedMessage = "Wrong year format, line number: 1";
        assertEquals(expectedMessage, actualMessage);
    }

    @Test
    @DisplayName("Test adding student from csv file when class has more than 2 letters")
    void generateStudents_givenFileWithMoreThanTwoLettersSchoolClass_shouldThrowWrongFileException() throws IOException {
        //given
        String filePath = FILE_WITH_GOOD_DATA;
        prepareData(filePath,
                "Lukasz,Szwacz,2021,1AA,123456790",
                "Bozena,Krewetka,2024,2A,987654321",
                "Wojciech,Mazur,2023,1A,111222333",
                "Dave,Szuwarek,2022,2A,123453339"
        );

        //then
        assertThrows(WrongFileException.class,() -> accountStudentCsvCreatorStrategy.generateStudents(filePath));
    }

    @Test
    @DisplayName("Test adding student from csv file when class no start with number")
    void generateStudents_givenFileWithNoNumberStartSchoolClass_shouldThrowWrongFileException() throws IOException {
        //given
        String filePath = FILE_WITH_GOOD_DATA;
        prepareData(filePath,
                "Lukasz,Szwacz,2021,A1,123456790",
                "Bozena,Krewetka,2024,2A,987654321",
                "Wojciech,Mazur,2023,1A,111222333",
                "Dave,Szuwarek,2022,2A,123453339"
        );

        //then
        assertThrows(WrongFileException.class,() -> accountStudentCsvCreatorStrategy.generateStudents(filePath));
    }

    @Test
    @DisplayName("Test adding student from csv file when parent number is longer than 9")
    void generateStudents_givenFileWithParentNumberLongerThan9_shouldThrowWrongFileException() throws IOException {
        //given
        String filePath = FILE_WITH_GOOD_DATA;
        prepareData(filePath,
                "Lukasz,Szwacz,2021,1A,1234567901",
                "Bozena,Krewetka,2024,2A,987654321",
                "Wojciech,Mazur,2023,1A,111222333",
                "Dave,Szuwarek,2022,2A,123453339"
        );

        //then
        assertThrows(WrongFileException.class,() -> accountStudentCsvCreatorStrategy.generateStudents(filePath));
    }

    @Test
    @DisplayName("Test adding student from csv file when parent number is shorter than 9")
    void generateStudents_givenFileWithParentNumberShorterThan9_shouldThrowWrongFileException() throws IOException {
        //given
        String filePath = FILE_WITH_GOOD_DATA;
        prepareData(filePath,
                "Lukasz,Szwacz,2021,1A,12345671",
                "Bozena,Krewetka,2024,2A,987654321",
                "Wojciech,Mazur,2023,1A,111222333",
                "Dave,Szuwarek,2022,2A,123453339"
        );

        //then
        assertThrows(WrongFileException.class,() -> accountStudentCsvCreatorStrategy.generateStudents(filePath));
    }

    @Test
    @DisplayName("Test adding student from csv file when parent number has letter")
    void generateStudents_givenFileWithParentNumberHasLetter_shouldThrowWrongFileException() throws IOException {
        //given
        String filePath = FILE_WITH_GOOD_DATA;
        prepareData(filePath,
                "Lukasz,Szwacz,2021,1A,12345678A",
                "Bozena,Krewetka,2024,2A,987654321",
                "Wojciech,Mazur,2023,1A,111222333",
                "Dave,Szuwarek,2022,2A,123453339"
        );

        //then
        assertThrows(WrongFileException.class,() -> accountStudentCsvCreatorStrategy.generateStudents(filePath));
    }

}