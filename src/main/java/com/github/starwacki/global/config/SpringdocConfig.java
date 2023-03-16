package com.github.starwacki.global.config;


import com.github.starwacki.components.account.controller.AccountController;
import com.github.starwacki.global.open_api.AccountControllerComponents;
import com.github.starwacki.global.open_api.GlobalComponents;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.tags.Tag;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class SpringdocConfig {

    @Bean
    public OpenAPI baseOpenApi() {
        return new OpenAPI().info(new Info()
                .title("Egrade documentation")
                .version("1.0.0")
                .description("Documentation for Egrade Spring boot project"))
                .tags(getDocumentationTags())
                .components(components());
    }



    private List<Tag> getDocumentationTags() {
        return List.of(new Tag()
                .name("account-controller")
                .description("Account controller is used for all account operations: add new account, delete account, change password etc.."));
    }

    private Components components() {
        return new Components()
                .responses(getAllApiResponses())
                .requestBodies(Map.of(
                        "accountStudentDTO", AccountControllerComponents.accountStudentDTORequestBody(),
                        "accountTeacherDTO", AccountControllerComponents.accountTeacherDTORequestBody()
                ));
    }

    private Map<String, ApiResponse> getAllApiResponses() {
        Map<String,ApiResponse> apiResponseMap = new HashMap<>();
        apiResponseMap.put("createStudentResponse", AccountControllerComponents.addStudentResponse());
        apiResponseMap.put("createStudentsResponse",AccountControllerComponents.addStudentsFromCSVFileResponse());
        apiResponseMap.put("createTeacherResponse",AccountControllerComponents.addTeacherResponse());
        apiResponseMap.put("getAccountByIdResponse",AccountControllerComponents.getAccountByIdResponse());
        apiResponseMap.put("deleteAccountByIdResponse",AccountControllerComponents.deleteAccountByIdResponse());
        apiResponseMap.put("badRequestDeleteAccountResponse",AccountControllerComponents.badRequestDeleteAccountResponse());
        apiResponseMap.put("badRequestGetAccountResponse",AccountControllerComponents.badRequestGetAccountResponse());
        apiResponseMap.put("badRequestAddStudentsFromCSVFileResponse",AccountControllerComponents.badRequestAddStudentsFromCSVFileResponse());
        apiResponseMap.put("badRequestResponse", GlobalComponents.badRequestResponse());
        apiResponseMap.put("forbiddenResponse",GlobalComponents.forbiddenResponse());
        apiResponseMap.put("accountNotFoundResponse",GlobalComponents.accountNotFoundResponse());
        apiResponseMap.put("badRequestChangeAccountPasswordResponse",AccountControllerComponents.badRequestChangeAccountPasswordResponse());
        return apiResponseMap;
    }


}
