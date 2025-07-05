package com.github.ramezch.spring_security;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
/*
@EnableJpaRepositories("com.github.ramezch.spring_security.repository")
@EntityScan("com.github.com.ramezch.spring_security.model")
These are if they were outside the main package. However, since they are inside, we don't need them
@EnableWebSecurity is optional in Spring Boot when you only need the default security behavior.
It becomes useful when you want to customize security configurations.
In contrast, in traditional Spring Framework (non-Boot), you often need to explicitly enable and configure security yourself
 */
public class SpringSecurityApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringSecurityApplication.class, args);
    }

}
