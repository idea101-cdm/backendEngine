package com.idea101.backendengine.common.exception;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import org.threeten.bp.LocalDateTime;

import java.net.URI;
import java.util.List;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ApiException.class)
    public ProblemDetail handleApiException(ApiException ex, HttpServletRequest request) {
        ProblemDetail prob = ProblemDetail.forStatus(ex.getStatus());
        prob.setTitle(ex.getClass().getSimpleName());
        prob.setDetail(ex.getMessage());
        prob.setInstance(URI.create(request.getRequestURI()));
        prob.setProperty("errorCode", ex.getErrorCode());
        prob.setProperty("timestamp", java.time.LocalDateTime.now().toString());
        return prob;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ProblemDetail handleValidation(MethodArgumentNotValidException ex, HttpServletRequest request) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.BAD_REQUEST);
        problem.setTitle("Validation Error");
        problem.setDetail("One or more fields are invalid");
        problem.setInstance(URI.create(request.getRequestURI()));
        problem.setProperty("timestamp", LocalDateTime.now().toString());

        List<String> fieldErrors = ex.getBindingResult().getFieldErrors().stream()
                .map(err -> err.getField() + ": " + err.getDefaultMessage())
                .toList();
        problem.setProperty("fieldErrors", fieldErrors);

        return problem;
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ProblemDetail handleNoResourceFound(NoResourceFoundException ex, HttpServletRequest request) {
        ProblemDetail problem = ProblemDetail.forStatus(HttpStatus.NOT_FOUND);
        problem.setTitle("Resource Not Found");
        problem.setDetail("Resource Not Found");
        problem.setInstance(URI.create(request.getRequestURI()));
        problem.setProperty("timestamp", LocalDateTime.now().toString());

        return problem;
    }

    @ExceptionHandler(Exception.class)
    public ProblemDetail handleGeneric(Exception ex, HttpServletRequest request) {
        ProblemDetail prob = ProblemDetail.forStatus(500);
        prob.setTitle("Internal Server Error");
        prob.setDetail("An unexpected error occurred");
        prob.setInstance(URI.create(request.getRequestURI()));
        prob.setProperty("errorCode", "INTERNAL_ERROR");
        prob.setProperty("timestamp", java.time.LocalDateTime.now().toString());
        return prob;
    }
}
