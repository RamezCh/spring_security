package com.github.ramezch.spring_security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UsernamePwdAuthenticationProvider implements AuthenticationProvider {

    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // We need to first load the User details from the system
        // We have already defined UserDetails so we just load it
        String username = authentication.getName();
        String rawPassword = authentication.getCredentials().toString();

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        if (userDetails == null) {
            throw new BadCredentialsException("Invalid username or password!");
        }
        // rawPassword.equals(userDetails.getPassword() won't work because remember its hashed in the DB and not raw text
        /*
        String hashedPwd = passwordEncoder.encode(rawPassword);
        if( hashedPwd.equals(userDetails.getPassword())) {
            authentication.setAuthenticated(true);
            return authentication;
        }
        the above approach won't work either because each password has a unique salt so we need to use passwordEncoder.matches method
         */
        if (passwordEncoder.matches(rawPassword, userDetails.getPassword())) {
            // We can add custom authentication here like age restrictions or location restrictions and so on.
            return new UsernamePasswordAuthenticationToken(
                    username,
                    rawPassword,
                    userDetails.getAuthorities()
            );
        } else {
            throw new BadCredentialsException("Invalid username or password!");
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        // We want to Support Username Password Authentication Style, so we use UsernamePasswordAuthenticationToken
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
