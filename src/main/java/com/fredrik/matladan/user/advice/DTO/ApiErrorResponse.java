package com.fredrik.matladan.user.advice.DTO;

import java.util.List;

public record ApiErrorResponse(
        int status,
        String error,
        List<ValidationError> errorDetailList){
}
