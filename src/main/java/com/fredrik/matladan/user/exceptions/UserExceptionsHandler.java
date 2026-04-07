package com.fredrik.matladan.user.exceptions;

import com.fredrik.matladan.user.advice.DTO.ApiErrorResponse;
import com.fredrik.matladan.user.advice.DTO.ValidationError;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.List;

@RestControllerAdvice
public class UserExceptionsHandler {
    private static final Logger logger = LoggerFactory.getLogger(UserExceptionsHandler.class);

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ApiErrorResponse> handleUserAlreadyExists(
            UserAlreadyExistsException exception,
            HttpServletRequest request
    ) {
        logger.warn("User Already exists error: {}", exception.getMessage());

        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(
                LocalDateTime.now(),
                request.getRequestURI(),
                HttpStatus.CONFLICT.value(),
                List.of(new ValidationError(
                        "username",
                        "Användarnamnet är redan taget"
                ))
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(apiErrorResponse);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ApiErrorResponse> handleEmailAlreadyExist(
            EmailAlreadyExistsException exception,
            HttpServletRequest request
    ) {
        logger.warn("Email was already in use error: {}", exception.getMessage());

        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(
                LocalDateTime.now(),
                request.getRequestURI(),
                HttpStatus.CONFLICT.value(),
                List.of(new ValidationError(
                        "email",
                        "E-postadressen används redan"
                ))
        );

        return ResponseEntity.status(HttpStatus.CONFLICT).body(apiErrorResponse);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleUserNotFound(
            UserNotFoundException exception,
            HttpServletRequest request
    ) {
        logger.warn("User was not found with error: {}", exception.getMessage());

        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(
                LocalDateTime.now(),
                request.getRequestURI(),
                HttpStatus.NOT_FOUND.value(),
                List.of(new ValidationError(
                        "user",
                        "Användaren hittades inte"
                ))
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiErrorResponse);
    }
}
