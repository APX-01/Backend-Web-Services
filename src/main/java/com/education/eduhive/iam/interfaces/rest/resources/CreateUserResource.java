package com.education.eduhive.iam.interfaces.rest.resources;

import com.education.eduhive.iam.domain.model.entities.Role;
import com.education.eduhive.iam.domain.model.valueobjects.Roles;

import java.util.Set;

public record CreateUserResource(
        String email,
        String firstName,
        String lastName,
        String password,
        Set<Role> roles
) {
    public CreateUserResource {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be null or blank");
        }
        if (firstName == null || firstName.isBlank()) {
            throw new IllegalArgumentException("First name cannot be null or blank");
        }
        if (lastName == null || lastName.isBlank()) {
            throw new IllegalArgumentException("Last name cannot be null or blank");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password cannot be null or blank");
        }
        if (roles == null || roles.isEmpty()) {
            throw new IllegalArgumentException("Roles cannot be null or empty");
        }
    }
}
