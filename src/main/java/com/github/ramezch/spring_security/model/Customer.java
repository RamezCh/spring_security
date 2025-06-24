package com.github.ramezch.spring_security.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

@Entity
// If the class name doesn't match table's name use @Table(name='table_name') annotation
@Getter @Setter
// Ctrl + F12 to see generated methods and file structure
public class Customer {
    @Id // This tells jpa framework know its primary key the Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // Indicates that the database will auto-generate the primary key value (e.g., using AUTO_INCREMENT or SERIAL).
    // JPA will not assign a value; instead, it retrieves the generated value after inserting the entity
    private long id;
    private String email;
    private String pwd;
    // If the field name does not match column use @Column(name="column_name") annotation e.g. @Column(name="role")
    private String role;
}
