package com.github.ramezch.spring_security.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.password.CompromisedPasswordChecker;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.password.HaveIBeenPwnedRestApiPasswordChecker;

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
        // flc is httpSecurityFormLoginConfigurer
        // We can disable formLogin and httpBasic with lambda expression flc -> flc.disable() instead of withDefaults()
        // we can rename flc in httpBasic with hbc for clearer meaning even tho both are acronyms
        http.formLogin(withDefaults());
        http.httpBasic(withDefaults());
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        // We haven't implemented passwordEncoder so we need to specify {noop} in password method for it to successfully login
        // Now we did so .password("{noop}12345") becomes .password("12345")
        // we can use {different Encryptions like MD4}
        UserDetails user = User.withUsername("user").password("{noop}12345").authorities("read").build();
        UserDetails admin = User.withUsername("admin")
                .password("{bcrypt}$2a$12$fbawcFvyWHKTHK5KXkd4Ju4XW3TjZnLY1bC0PlpVRhTP1uTpB14PO")
                .authorities("admin").build();
        return new InMemoryUserDetailsManager(user, admin);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        // It is possible to directly return new BCryptPasswordEncoder(); but lower approach better because in few years maybe bcrypt isn't recommended
        // Uses BCrypt Encoder as it is the safest or at least so is thought
        // Default is 12 rounds of salt
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    // What if I want users to implement secure passwords and not ones like 12345, 54321, password?
    // This is where the CompromisedPasswordChecker comes in
    // One of the methods is to use Have I been Pwned API to check if the password was in a previous data breach
    // We can also pass our own implementation instead of Have I been pwned and return if it has been leaked or not
    // Now the above users won't be able to login because they weren't created due to compromised passwords
    @Bean
    public CompromisedPasswordChecker compromisedPasswordChecker() {
        return new HaveIBeenPwnedRestApiPasswordChecker();
    }
}
