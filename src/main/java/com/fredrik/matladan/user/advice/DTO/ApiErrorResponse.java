package com.fredrik.matladan.user.advice.DTO;

import java.time.LocalDateTime;
import java.util.List;

public record ApiErrorResponse(
        //? Time when it happend
        LocalDateTime timestamp,
        //? Path, which controller path did they use
        //? Like /api/users/register
        String path,
        int status,
        String error,
        List<ValidationError> errorDetailList

){

}
