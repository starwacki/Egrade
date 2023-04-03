package com.github.starwacki.common.handlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class ValidationArgumentNotValidExceptionAdvice {

    private static final Logger logger = LoggerFactory.getLogger(ValidationArgumentNotValidExceptionAdvice.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String,String>> methodArgumentNotValidException(MethodArgumentNotValidException exception) {
        Map<String,String> errorsMap =
                exception.getBindingResult().getFieldErrors()
                        .stream()
                        .collect(Collectors.toMap(
                                FieldError -> FieldError.getField(),
                                fieldError -> "Invalid value: " + fieldError.getDefaultMessage(),
                                (value1, value2) -> value2,
                                LinkedHashMap::new
                        ));
        logger.info(exception.getMessage());
        return ResponseEntity.badRequest().body(errorsMap);
    }


}
