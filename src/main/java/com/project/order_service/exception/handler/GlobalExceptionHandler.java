package com.project.order_service.exception.handler;

import com.project.order_service.exception.BaseHttpException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.Instant;

public class GlobalExceptionHandler {
    @ExceptionHandler(BaseHttpException.class)
    public ProblemDetail handleBaseException(BaseHttpException ex) {
        ex.printStackTrace();

        var problemDetail = ProblemDetail.forStatusAndDetail(ex.getStatus(), ex.getMessage());

        problemDetail.setTitle("Business error");
        problemDetail.setProperty("timestamp", Instant.now());

        return problemDetail;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleUnhandledException(Exception ex) {
        ex.printStackTrace();

        var problemDetail = ProblemDetail.forStatusAndDetail(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Unpredictable error thrown"
        );

        problemDetail.setTitle("Internal server error");
        problemDetail.setProperty("timestamp", Instant.now());

        return problemDetail;
    }
}
