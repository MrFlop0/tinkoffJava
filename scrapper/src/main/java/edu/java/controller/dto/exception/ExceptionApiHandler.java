package edu.java.controller.dto.exception;

import edu.java.controller.dto.Response.ApiErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.util.Arrays;
import java.util.stream.Collectors;

@RestControllerAdvice
public class ExceptionApiHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiErrorResponse> runtimeException(RuntimeException exception) {
        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(new ApiErrorResponse(
                    "Internal Server Error",
                    HttpStatus.INTERNAL_SERVER_ERROR.toString(),
                    HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                    exception.getMessage(),
                    Arrays.stream(exception.getStackTrace())
                        .map(StackTraceElement::toString)
                        .collect(Collectors.toList())
                )
            );
    }
}