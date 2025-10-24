package com.fredrik.matladan.user;


public record CustomUser<id>(

        int id,
        String email,
        String password
){
}
