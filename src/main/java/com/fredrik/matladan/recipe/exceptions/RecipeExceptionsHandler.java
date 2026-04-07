package com.fredrik.matladan.recipe.exceptions;

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
public class RecipeExceptionsHandler {

    private static final Logger logger = LoggerFactory.getLogger(RecipeExceptionsHandler.class);

    @ExceptionHandler(RecipeNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleRecipeNotFoundException(
            RecipeNotFoundException exception,
            HttpServletRequest request
    ) {
        logger.warn("Recipe not found: {}", exception.getMessage());

        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(
                LocalDateTime.now(),
                request.getRequestURI(),
                HttpStatus.NOT_FOUND.value(),
                List.of(new ValidationError(
                        "recipe",
                        "Receptet hittades inte"
                ))
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiErrorResponse);
    }
}
