package com.github.ramezch.spring_security.repository;

import com.github.ramezch.spring_security.model.Customer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends CrudRepository<Customer, Long> {
    // This method tells Spring Data JPA to auto-generate a query that finds a Customer by its 'email' property.
    // The "findByEmail" method name tells Spring Data JPA to look for a field named "email" in the Customer entity class.
    // If the @Column(name = "customer_email") annotation is used in the Customer entity,
    // it maps the 'email' field to a column named 'customer_email' in the database.
    // So:
    // - Method name refers to the ENTITY FIELD (Java property)
    // - Actual SQL uses the COLUMN NAME defined via @Column (if present)
    Optional<Customer> findByEmail(String email);
}
