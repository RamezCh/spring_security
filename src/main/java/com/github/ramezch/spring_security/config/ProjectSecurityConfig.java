package com.github.ramezch.spring_security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
public class ProjectSecurityConfig {
    // Ctrl + Alt + O deletes all unused imports
    // You can let intelliJ do this automatically by setting optimize imports on the fly to true
    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        // disabled cross-site request forgery for postman - will implement it later
        http.csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(requests -> requests
                .requestMatchers("/myAccount", "/myBalance", "/myLoans", "/myCards").authenticated()
                .requestMatchers("/notices", "/contact", "/error", "/register").permitAll());
        http.formLogin(withDefaults());
        http.httpBasic(withDefaults());
        return http.build();
    }

    // Since we are connected to DB now we do not need to provide users manually
    // JdbcUserDetailsManager takes the dataSource that is created from application.properties that we set
    // so it knows how to connect to DB and load the users
    /*
    Commented out because we implemented custom table at WhateverYouWantUserDetailsService class
    @Bean
    public UserDetailsService userDetailsService(DataSource dataSource) {
        // This uses the exact tables present in Jdbc which is users and authorities
        // We want our own custom tables right?
        return new JdbcUserDetailsManager(dataSource);
    }
    */

    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }


    @Bean
    public CompromisedPasswordChecker compromisedPasswordChecker() {
        return new HaveIBeenPwnedRestApiPasswordChecker();
    }
}
