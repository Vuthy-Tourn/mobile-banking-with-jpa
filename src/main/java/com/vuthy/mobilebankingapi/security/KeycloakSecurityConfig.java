package com.vuthy.mobilebankingapi.security;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class KeycloakSecurityConfig {

    private final String ROLE_ADMIN = "ADMIN";
    private final String ROLE_STAFF = "STAFF";
    private final String ROLE_CUSTOMER = "CUSTOMER";

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {

        // Make all endpoints secured
        http.authorizeHttpRequests(authorizeRequests -> authorizeRequests
                .requestMatchers(HttpMethod.PUT,"/api/v1/customers/**")
                .hasAnyRole(ROLE_ADMIN, ROLE_STAFF)
                .requestMatchers(HttpMethod.DELETE,"/api/v1/customers/**")
                .hasAnyRole(ROLE_ADMIN)
                .requestMatchers(HttpMethod.POST,"/api/v1/customers/**")
                .hasAnyRole(ROLE_ADMIN,ROLE_STAFF)
                .requestMatchers(HttpMethod.GET,"/api/v1/customers/**")
                .hasAnyRole(ROLE_ADMIN, ROLE_STAFF,ROLE_CUSTOMER)
                .requestMatchers("/api/v1/accounts/**")
                .hasAnyRole(ROLE_ADMIN, ROLE_STAFF,ROLE_CUSTOMER)
                .requestMatchers("/media/**").permitAll()
                .anyRequest()
                .authenticated());

        // Disable form login
        http.formLogin(form -> form.disable());

        // Set security mechanism = OAuth2
        http.oauth2ResourceServer(oauth2 -> oauth2.jwt(Customizer.withDefaults()));

        // CSRF common protection
        http.csrf(token -> token.disable());

        // Make Stateless API
        http.sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    @Bean
    public JwtAuthenticationConverter jwtAuthenticationConverterForKeycloak() {
        Converter<Jwt, Collection<GrantedAuthority>> jwtGrantedAuthoritiesConverter = jwt -> {
            Map<String, Collection<String>> realmAccess = jwt.getClaim("realm_access");
            Collection<String> roles = realmAccess.get ("roles");

            return roles.stream()
                    .map (role -> new SimpleGrantedAuthority("ROLE_" + role))
            .collect(Collectors.toList());
        };
        JwtAuthenticationConverter jwtAuthenticationConverter = new JwtAuthenticationConverter();
        jwtAuthenticationConverter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter);
        return jwtAuthenticationConverter;

    }
}
