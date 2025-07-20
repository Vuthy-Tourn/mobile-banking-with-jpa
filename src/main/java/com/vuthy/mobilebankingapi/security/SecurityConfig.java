package com.vuthy.mobilebankingapi.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // Make all endpoints secured
        http.authorizeHttpRequests(authorizeRequests -> authorizeRequests
                .anyRequest()
                .authenticated());

        // Disable form login
        http.formLogin(form -> form.disable());

        // Set security mechanism = HTTP Basic  Authentication => jwt, OAuth2
        http.httpBasic(Customizer.withDefaults());

        // CSRF common protection
        http.csrf(token -> token.disable());

        // Make Stateless API
        http.sessionManagement(session -> session
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }
}
