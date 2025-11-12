package com.fredrik.matladan.item.exceptions;

import com.fredrik.matladan.user.advice.DTO.ApiErrorResponse;
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
public class ItemExceptionsHandler {
    private static final Logger logger = LoggerFactory.getLogger(ItemExceptionsHandler.class);

    //? Exception handler for ItemNotFoundException
    //? This will trigger in case the Item the user searches for isn't there
    @ExceptionHandler(ItemNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleItemNotFoundException
    (ItemNotFoundException exception,
     HttpServletRequest request)
    {
        //? Log the error
        logger.warn("Item was not found with error: {}",exception.getMessage());

        //? Create the ApiErrorResponse
        //? The ApiErrorResponse is used to send the error to the user
        //? It will mark the time and date of the error
        //? It will contain the path of the request
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(
                LocalDateTime.now(),
                request.getRequestURI(),
                HttpStatus.NOT_FOUND.value(),
                List.of()
        );

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(apiErrorResponse);
    }

    @ExceptionHandler(UserIsNotLoggedInException.class)
    public ResponseEntity<ApiErrorResponse> handleUserNotLoggedinException
            (UserIsNotLoggedInException exception,
             HttpServletRequest request) {
        logger.warn("User was not logged in error: {}",exception.getMessage());

        //? Create the ApiErrorResponse
        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(
                LocalDateTime.now(),
                request.getRequestURI(),
                HttpStatus.UNAUTHORIZED.value(),
                List.of()
        );

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(apiErrorResponse);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalArguement(
            IllegalArgumentException exception,
            HttpServletRequest request
    ){
        logger.warn("Illegal argument error: {}",exception.getMessage());

        ApiErrorResponse apiErrorResponse = new ApiErrorResponse(
                LocalDateTime.now(),
                request.getRequestURI(),
                HttpStatus.BAD_REQUEST.value(),
                List.of()
        );

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(apiErrorResponse);
    }
}
