package com.github.starwacki.global_exceptions;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.stream.Collectors;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class ValidationArgumentNotValidExceptionAdvice {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> methodArgumentNotValidException(MethodArgumentNotValidException exception) {
        Map<String,String> errorsMap =
                exception.getBindingResult().getFieldErrors()
                        .stream()
                        .collect(Collectors.toMap(
                                FieldError -> FieldError.getField(),
                                fieldError -> "Invalid value: " + fieldError.getDefaultMessage()
                        ));
        System.out.println(errorsMap);
        return ResponseEntity.badRequest().body(errorsMap);
    }


}
