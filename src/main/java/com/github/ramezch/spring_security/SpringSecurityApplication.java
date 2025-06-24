package com.github.ramezch.spring_security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
/*
@EnableJpaRepositories("com.github.ramezch.spring_security.repository")
@EntityScan("com.github.com.ramezch.spring_security.model")
These are if they were outside the main package, but since they are inside we don't need them
 */
public class SpringSecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringSecurityApplication.class, args);
    }

}
