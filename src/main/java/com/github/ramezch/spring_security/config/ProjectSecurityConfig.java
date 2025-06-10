package com.github.ramezch.spring_security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class ProjectSecurityConfig {

    /**
     * Configuration notes:
     * - By default, Spring Security does NOT automatically protect all endpoints.
     *   You must explicitly define which URLs require authentication.
     * - The `.authenticated()` rule means the user must be logged in (authenticated)
     *   to access the specified URL(s).
     * - The `.permitAll()` rule allows unrestricted access to the specified URL(s),
     *   even for unauthenticated users.
     * - The `.denyAll()` rule blocks access to the specified URL(s), even for authenticated users.
     *   This is rarely used unless you want to completely disable certain endpoints.
     * - `/error` is a built-in Spring Boot endpoint that displays error details
     *   (like stack traces in development mode). Be cautious about exposing it in production.
     */

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(requests -> requests
                .requestMatchers("/myAccount", "/myBalance", "/myLoans", "/myCards").authenticated()
                .requestMatchers("/notices", "/contact", "/error").permitAll());
        http.formLogin(withDefaults());
        http.httpBasic(withDefaults());
        return http.build();
    }
}
