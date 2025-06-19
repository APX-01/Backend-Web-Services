package com.education.eduhive.iam.domain.model.commads;

import com.education.eduhive.iam.domain.model.valueobjects.Role;

public record CreateUserCommand(
        String email,
        String firstName,
        String lastName,
        String password,
        Role role
) {

    public CreateUserCommand {
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
        if (role == null ) {
            throw new IllegalArgumentException("Role cannot be null");
        }
    }

}
