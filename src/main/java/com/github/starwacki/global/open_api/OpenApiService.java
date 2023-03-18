package com.github.starwacki.global.open_api;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class OpenApiService {

    public Map<String, ApiResponse> getAllApiResponses() {
        Map<String,ApiResponse> responsesMap = new HashMap<>();
        addAllAccountControllerResponses(responsesMap);
        addAllStudentControllerResponses(responsesMap);
        addAllGlobalResponses(responsesMap);

        return responsesMap;
    }

    public Map<String, RequestBody> getAllApiRequestBodies() {
        Map<String, RequestBody> requestsMap = new HashMap<>();
        addAllAccountControllerRequestBodies(requestsMap);
        addAllStudentControllerRequestBodies(requestsMap);
        return  requestsMap;
    }

    private void addAllAccountControllerRequestBodies(Map<String, RequestBody> requestsMap) {
        requestsMap.put("accountStudentDTO", AccountControllerComponents.accountStudentDTORequestBody());
        requestsMap.put("accountTeacherDTO", AccountControllerComponents.accountTeacherDTORequestBody());
    }

    private void addAllGlobalResponses(Map<String,ApiResponse> apiResponseMap) {
        apiResponseMap.put("badRequestResponse", GlobalComponents.badRequestResponse());
        apiResponseMap.put("forbiddenResponse",GlobalComponents.forbiddenResponse());
        apiResponseMap.put("accountNotFoundResponse",GlobalComponents.accountNotFoundResponse());
    }

    private void addAllAccountControllerResponses(Map<String,ApiResponse> apiResponseMap) {
        apiResponseMap.put("createStudentResponse", AccountControllerComponents.addStudentResponse());
        apiResponseMap.put("createStudentsResponse",AccountControllerComponents.addStudentsFromCSVFileResponse());
        apiResponseMap.put("createTeacherResponse",AccountControllerComponents.addTeacherResponse());
        apiResponseMap.put("getAccountByIdResponse",AccountControllerComponents.getAccountByIdResponse());
        apiResponseMap.put("deleteAccountByIdResponse",AccountControllerComponents.deleteAccountByIdResponse());
        apiResponseMap.put("badRequestDeleteAccountResponse",AccountControllerComponents.badRequestDeleteAccountResponse());
        apiResponseMap.put("badRequestGetAccountResponse",AccountControllerComponents.badRequestGetAccountResponse());
        apiResponseMap.put("badRequestAddStudentsFromCSVFileResponse",AccountControllerComponents.badRequestAddStudentsFromCSVFileResponse());
        apiResponseMap.put("badRequestChangeAccountPasswordResponse",AccountControllerComponents.badRequestChangeAccountPasswordResponse());
    }

    private void addAllStudentControllerResponses(Map<String,ApiResponse> apiResponseMap) {
        apiResponseMap.put("getAllStudentsFromClassResponse", StudentControllerComponents.getAllStudentsFromClassResponse());
        apiResponseMap.put("changeStudentClassResponse", StudentControllerComponents.changeStudentClassResponse());
        apiResponseMap.put("studentNotFoundResponse", StudentControllerComponents.studentNotFoundResponse());
        apiResponseMap.put("addGradeToStudentResponse",StudentControllerComponents.addGradeToStudentResponse());
        apiResponseMap.put("getStudentGradeResponse",StudentControllerComponents.getStudentGradeResponse());
        apiResponseMap.put("updateStudentGradeResponse",StudentControllerComponents.updateStudentGradeResponse());
        apiResponseMap.put("deleteStudentGradeResponse",StudentControllerComponents.deleteStudentGradeResponse());
        apiResponseMap.put("getStudentGradesResponse",StudentControllerComponents.getStudentGradesResponse());
        apiResponseMap.put("getStudentSubjectGrades",StudentControllerComponents.getStudentSubjectGrades());
    }

    private void addAllStudentControllerRequestBodies(Map<String, RequestBody> requestsMap) {
        requestsMap.put("changeStudentClassRequestBody", StudentControllerComponents.changeStudentClassRequestBody());
        requestsMap.put("updateStudentGradeRequestBody", StudentControllerComponents.updateStudentGradeRequestBody());
    }
}
