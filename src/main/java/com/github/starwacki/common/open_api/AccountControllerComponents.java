package com.github.starwacki.common.open_api;

import com.github.starwacki.components.account.dto.AccountStudentDTO;
import io.swagger.v3.oas.models.examples.Example;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.springframework.http.MediaType;
import java.util.Map;


class AccountControllerComponents {

    private AccountControllerComponents() {}

    static ApiResponse addStudentResponse() {
        return new ApiResponse()
                .description("Student and Parent account was successfully added")
                .content(new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
                        new io.swagger.v3.oas.models.media.MediaType().
                                addExamples("default",new Example()
                                        .description("Username is create by algorithm: firstname + . + lastname + STU + number of students in database, First password" +
                                                " is create by 10 random letters or numbers. Parent account username is create with algorithm:" +
                                                "firstname + . + lastname + RO + number of students in database, first parent account password is create randomly like student account password " +
                                                "for example, if student username is: Firstname.LastnameSTU1, then parent username: Firstname.LastnameRO1")
                                        .value("{\"id\":1,\"firstname\":\"Firstname\",\"lastname\":\"Lastname\",\"username\":\"Firstname.LastnameSTU1\",\"password\":\"ocXtwAjBwA\",\"accountType\":\"STUDENT\"}"))));
    }

    static ApiResponse addStudentsFromCSVFileResponse() {
        return new ApiResponse()
                .description("Students and Parents account were successfully added")
                .content(new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
                        new io.swagger.v3.oas.models.media.MediaType().
                                addExamples("default",new Example()
                                        .description("Username is create by algorithm: firstname + . + lastname + STU + number of students in database, First password" +
                                                " is create by 10 random letters or numbers. Parent account username is create with algorithm:" +
                                                "firstname + . + lastname + RO + number of students in database, first parent account password is create randomly like student account password " +
                                                "for example, if student username is: Firstname.LastnameSTU1, then parent username: Firstname.LastnameRO1")
                                        .value("[{\"id\":1,\"firstname\":\"Firstname\",\"lastname\":\"Lastname\",\"username\":\"Firstname.LastnameSTU1\",\"password\":\"ocXtwAjBwA\",\"accountType\":\"STUDENT\"}," +
                                                "{\"id\":2,\"firstname\":\"Firstname\",\"lastname\":\"Lastname\",\"username\":\"Firstname.LastnameSTU2\",\"password\":\"FSA4jQ1k91\",\"accountType\":\"STUDENT\"}]"))));
    }

    static ApiResponse addTeacherResponse() {
        return new ApiResponse()
                .description("Teacher account was successfully added")
                .content(new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
                        new io.swagger.v3.oas.models.media.MediaType().
                                addExamples("default",new Example()
                                        .description("Username is create by algorithm: firstname + . + lastname + NAU + number of teachers in database, First password" +
                                                " is create by 10 random letters or numbers.")
                                        .value("{\"id\":1,\"firstname\":\"Firstname\",\"lastname\":\"workPhone\",\"username\":\"Firstname.LastnameNAU1\",\"password\":\"ocXtwAjBwA\",\"accountType\":\"TEACHER\"}"))));
    }

    static ApiResponse getAccountByIdResponse() {
        return new ApiResponse()
                .description("Account successfully retrieved")
                .content(new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
                        new io.swagger.v3.oas.models.media.MediaType().
                                addExamples("default",new Example()
                                        .value("{\"id\":1,\"firstname\":\"Firstname\",\"lastname\":\"workPhone\",\"username\":\"Firstname.LastnameNAU1\",\"password\":\"ocXtwAjBwA\",\"accountType\":\"TEACHER\"}"))));
    }

    static ApiResponse deleteAccountByIdResponse() {
        return new ApiResponse()
                .description("Account successfully deleted")
                .content(new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
                        new io.swagger.v3.oas.models.media.MediaType().
                                addExamples("default",new Example()
                                        .value("{\"id\":1,\"firstname\":\"Firstname\",\"lastname\":\"workPhone\",\"username\":\"Firstname.LastnameNAU1\",\"password\":\"ocXtwAjBwA\",\"accountType\":\"TEACHER\"}"))));
    }

    static ApiResponse badRequestAddStudentsFromCSVFileResponse() {
        return new ApiResponse()
                .description("Bad request - file is wrong or  data in file is incorrect")
                .content(new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
                        new io.swagger.v3.oas.models.media.MediaType().
                                examples(Map.of(
                                        "data", new Example().value("{ \"line\": \"Line is empty or not have call, line number: 1\",\"year\": \"Wrong year format, line number: 1\"}"
                                                )))));
    }

    static ApiResponse badRequestDeleteAccountResponse() {
        return new ApiResponse()
                .description("Bad request - data or input exception")
                .content(new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
                        new io.swagger.v3.oas.models.media.MediaType().
                                addExamples(
                                        "IllegalOperation", new Example().value("Illegal operation type: DELETE for role: ADMIN")
                                )));
    }

    static ApiResponse badRequestGetAccountResponse() {
        return new ApiResponse()
                .description("Bad request - data or input exception")
                .content(new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
                        new io.swagger.v3.oas.models.media.MediaType().
                                addExamples(
                                        "IllegalOperation", new Example().value("Illegal operation type: GET for role: ADMIN")
                                )));
    }

    static ApiResponse badRequestChangeAccountPasswordResponse() {
        return new ApiResponse()
                .description("Bad request - data or input exception")
                .content(new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
                        new io.swagger.v3.oas.models.media.MediaType().
                                examples(
                                        Map.of(
                                                "Illegal operation", new Example().value("Illegal operation type: PUT for role: ADMIN"),
                                                "Wrong password", new Example().value("Password not same")
                                                )
                                )));
    }

    static RequestBody accountStudentDTORequestBody() {
        return new RequestBody()
                .description( " \"year\" - the current year of the student necessary to be added to the appropriate class" +
                        " \"className\" - class name, numbered as follows: NUMBER(0-9) + BIG LETTER (A-Z)")
                .content(new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
                        new io.swagger.v3.oas.models.media.MediaType()
                                .schema(new Schema<>().jsonSchemaImpl(AccountStudentDTO.class))
                                .addExamples("default",new Example()
                                        .value("{\"firstname\":\"Firstname\",\"lastname\":\"Lastname\",\"year\":\"2023\",\"className\":\"2A\",\"parentPhoneNumber\":\"111222333\"}"))));
    }

    static RequestBody accountTeacherDTORequestBody() {
        return new RequestBody()
                .content(new Content().addMediaType(MediaType.APPLICATION_JSON_VALUE,
                        new io.swagger.v3.oas.models.media.MediaType()
                                .schema(new Schema<>())
                                .addExamples("default",new Example()
                                        .value("{\"firstname\":\"Firstname\",\"lastname\":\"Lastname\",\"workphone\":\"111222333\",\"email\":\"teacher@wp.pl\",\"subject\":\"MATHS\"}"))));
    }

}
