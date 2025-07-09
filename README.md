# Spring Security

This is a test field where I apply different security principles and methods starting from the most basic to advanced.

## HTTPS
Any user on a web application or mobile would look for HTTPS to feel the sense of security.

The default behavior is accepting both HTTP and HTTPS, but we want only HTTPS. Turning it on is straightforward. We just add redirectToHttps(withDefaults()) to the defaultSecurityFilterChain.

## Exception Handling in Spring Security Framework
Spring Security provides a robust and flexible mechanism for handling exceptions that occur during authentication and authorization processes. Unlike general application exceptions, Spring Security only directly handles security-related exceptions, specifically those related to authentication failures and access control violations.

The core component responsible for this behavior is the ExceptionTranslationFilter. This filter sits early in the Spring Security filter chain and acts as a global exception handler for security-related issues.

### How It Works: The Flow
When a request passes through the Spring Security filter chain, any unhandled exception (especially those thrown by security components) is caught by the ExceptionTranslationFilter.
This filter inspects the type of exception:
1. If it’s an instance of AuthenticationException (401), it indicates that the user is not authenticated.
2. If it’s an instance of AccessDeniedException (403), it means the user is authenticated but lacks enough permissions to access the requested resource.

Based on the exception type, the filter delegates to the appropriate strategy:
- For AuthenticationException: Invokes the AuthenticationEntryPoint
- For AccessDeniedException: Invokes the AccessDeniedHandler

## Authentication Entry Point
It is used by ExceptionTranslationFilter and has only a single abstract method commence. There are multiple implementations of this class like BasicAuthenticationEntryPoint.

If we want to customize the response from AuthenticationException, we create a new clas that implements AuthenticationEntryPoint class.


