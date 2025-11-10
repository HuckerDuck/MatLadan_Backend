package com.fredrik.matladan.securityconfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        //? BCrypt is usually the default way you want to do this
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain (HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests (auth -> auth
                        //? Anywone is allowed to create a account
                        //? It will automaticly be a user with the role USER
                        .requestMatchers ("/api/users/register").permitAll()
                        .anyRequest().authenticated ()
                )
                .formLogin(form -> form.disable())
                .httpBasic(withDefaults());

        return http.build();
    }
}
