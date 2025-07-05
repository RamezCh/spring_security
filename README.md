# Spring Security

This is a test field where I apply different security principles and methods starting from the most basic to advanced.

## AuthenticationProvider
The default one is DaoAuthenticationProvider, which works well for standard username/password authentication. However, in certain cases, we may need to implement our own custom AuthenticationProvider.

### What is an AuthenticationProvider?
It’s a core interface in Spring Security that handles user authentication. It has only two methods:

- authenticate (Authentication authentication):
Takes an Authentication object (which contains credentials like username and password) and verifies if the user is valid. If successful, returns a fully populated Authentication object with authenticated = true.
- supports (Class<?> authentication):
Checks whether this provider can handle the given type of Authentication object. For example, UsernamePasswordAuthenticationToken.

### How Does ProviderManager Know Which Provider to Use?
Spring Security uses the ProviderManager, which is responsible for delegating authentication requests to the correct AuthenticationProvider.

It identifies the right provider by checking each one’s supports() method against the incoming Authentication object’s class type.

### Common Authentication Tokens
There are several types of Authentication tokens used in different scenarios:

- UsernamePasswordAuthenticationToken: Used for standard login forms.
- TestingAuthenticationToken: For unit testing purposes only.
- RememberMeAuthenticationToken: When the "Remember Me" functionality is enabled.
- AnonymousAuthenticationToken: Represents unauthenticated users.
- OAuth2AuthenticationToken: For OAuth2-based authentication.
- JwtAuthenticationToken: For JWT token-based systems.

**Note**: As of now, only DaoAuthenticationProvider supports UsernamePasswordAuthenticationToken.

### When Do We Need a Custom AuthenticationProvider?
While DaoAuthenticationProvider works well for basic authentication, it does not support custom business rules or complex conditions. You should create your own AuthenticationProvider when you need to enforce:

- Group restrictions (e.g., specific roles or departments).
- Age limits.
- Country or location-based access control.
- Device-specific authentication.
- Multifactor authentication logic.

This allows you to add additional checks beyond just username and password validation.

### Quick Workflow Summary
User logs in → Request is intercepted by Spring Security filters → Reaches ProviderManager → Manager determines which AuthenticationProvider to use based on the type of Authentication object → Provider performs the actual authentication.

### Real-World Implementations

In real enterprise applications, you’ll often see multiple authentication mechanisms implemented together. These may include:

- Standard username & password (done here)
- OAuth2 for third-party or social logins
- JAAS (Java Authentication and Authorization Service) for legacy system integration

Each of these requires its own AuthenticationProvider implementation.

### Throughout the development of the project, there are different phases.
- DEV = Development
- SIT = System Integration Testing
- UAT = User Acceptance Testing (Real users test the software to see if it meets their needs)
- PROD = Production

Dev, SIT, UAT are called Lower environments because this is where testing happens.

Regardless of what phase you are at, the user will be required to enter his credentials during log in.

The difference is in PROD we have real users and Lower environments we have Devs, QAs and so on

Usually, in Lower environments they ask us for fixed credentials so they have an easier workflow by accessing the application with any password.

Inside Spring Boot we have a Profiles concept that runs things conditionally which we can use to let Lower environments users enter any password and login.