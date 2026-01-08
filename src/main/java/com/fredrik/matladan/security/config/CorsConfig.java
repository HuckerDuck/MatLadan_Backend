package com.fredrik.matladan.security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
//**
// CORS (Cross-Origin Resource Sharing) CONFIGURATION
//
// This configureation is needed to allow the backend to
// accept requests from other websites or apps like my React Native App
//
//
// Without CORS, the backend is gonna refuse the connection
// and I turn won't be able to connect to the backend and access the database
// /
public class CorsConfig {
    @Bean
    //? This Primary annotation is needed to make sure that this bean is used instead of the default one
    //? Since Spring got two you will get errors in the SecurityConfig if you don't use this
    //? This is mostly if you wanna do @AllArgsConstructor on the SecurityConfig class
    @Primary
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration CorsConfig = new CorsConfiguration();

        //? To use with React Native (EXPO FrameWork)
        //? WE allow connection
        //! For debug purposes now, check if this is alright later


        //? If you got a website you can set this to allow connections from that website
        // .setAllowOrigin
        // Then the website can connect to the backend


        // ALLOWED HTTP METHOD FROM THE USER FROM THE FRONTEND
        // GET: Read data
        // POST: Create data
        // PUT: Update data
        // PATCH: Update data partially
        // DELETE: Delete data
        // /
        CorsConfig.setAllowedMethods(List.of("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));

        //*
        // ALLOWED HEADERS
        // "*" allows all the headers from the request from the frontend
        // This includes and is not limited to
        // - Authorization
        // - Content-Type: with application properties from a JSON file
        // - We allow all because the frontend will still need to manually add the
        // JWT token to the headers/

        //! For now this is all very open, need to change this later
        //! When this pushes to production
        //! This is mearly for development purposes and
        //! Hopefully to see that it wörks with Render Native properly

        CorsConfig.setAllowedHeaders(List.of("*"));

        //? Putting this to false since am using React Native Expo
        CorsConfig.setAllowCredentials(false);
        CorsConfig.setAllowedOriginPatterns(List.of("*"));

        //? Then we register it for all diffrent path that there are
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", CorsConfig);

        return source;
    }

}
