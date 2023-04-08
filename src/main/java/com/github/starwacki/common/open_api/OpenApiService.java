package com.github.starwacki.common.open_api;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
class OpenApiService {

    Map<String, ApiResponse> getAllApiResponses() {
        Map<String,ApiResponse> responsesMap = new HashMap<>();
        addAllAccountControllerResponses(responsesMap);
        addAllStudentControllerResponses(responsesMap);
        addAllTeacherControllerResponses(responsesMap);
        addAllAuthenticationControllerResponses(responsesMap);
        addAllGlobalResponses(responsesMap);
        return responsesMap;
    }

    Map<String, RequestBody> getAllApiRequestBodies() {
        Map<String, RequestBody> requestsMap = new HashMap<>();
        addAllAccountControllerRequestBodies(requestsMap);
        addAllStudentControllerRequestBodies(requestsMap);
        addAllTeacherControllerRequestBodies(requestsMap);
        addAllAuthenticationControllerRequestBodies(requestsMap);
        return  requestsMap;
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

    private void addAllTeacherControllerResponses(Map<String,ApiResponse> apiResponseMap) {
        apiResponseMap.put("getTeacherClassesResponse", TeacherControllerComponents.getTeacherClassesResponse());
        apiResponseMap.put("teacherNotFoundResponse", TeacherControllerComponents.teacherNotFoundResponse());
        apiResponseMap.put("addSchoolClassToTeacherResponse", TeacherControllerComponents.addSchoolClassToTeacherResponse());
        apiResponseMap.put("getAllTeachersInformationResponse", TeacherControllerComponents.getAllTeachersInformationResponse());

    }

    private void addAllAuthenticationControllerResponses(Map<String,ApiResponse> apiResponseMap) {
        apiResponseMap.put("okAuthenticateUserResponse", AuthenticationControllerComponents.okAuthenticateUserResponse());
        apiResponseMap.put("badRequestAuthenticateUserResponse",AuthenticationControllerComponents.badRequestAuthenticateUserResponse());
        apiResponseMap.put("unauthorizedAuthenticateUserResponse",AuthenticationControllerComponents.unauthorizedAuthenticateUserResponse());

    }

    private void addAllAccountControllerRequestBodies(Map<String, RequestBody> requestsMap) {
        requestsMap.put("accountStudentDTO", AccountControllerComponents.accountStudentDTORequestBody());
        requestsMap.put("accountTeacherDTO", AccountControllerComponents.accountTeacherDTORequestBody());
    }

    private void addAllStudentControllerRequestBodies(Map<String, RequestBody> requestsMap) {
        requestsMap.put("changeStudentClassRequestBody", StudentControllerComponents.changeStudentClassRequestBody());
        requestsMap.put("updateStudentGradeRequestBody", StudentControllerComponents.updateStudentGradeRequestBody());
    }

    private void addAllTeacherControllerRequestBodies(Map<String, RequestBody> requestsMap) {
        requestsMap.put("addSchoolClassToTeacherRequestBody", TeacherControllerComponents.addSchoolClassToTeacherRequestBody());
    }

    private void addAllAuthenticationControllerRequestBodies(Map<String, RequestBody> requestsMap) {
        requestsMap.put("authenticateUserRequestBody", AuthenticationControllerComponents.authenticateUserRequestBody());
    }


}
