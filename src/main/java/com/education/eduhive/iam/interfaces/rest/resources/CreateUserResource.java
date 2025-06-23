package com.education.eduhive.iam.interfaces.rest.resources;

import com.education.eduhive.iam.domain.model.valueobjects.Role;

public record CreateUserResource(
        String email,
        String firstName,
        String lastName,
        String password,
        Role role
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
        if (role == null) {
            throw new IllegalArgumentException("Role cannot be null");
        }
    }
}
