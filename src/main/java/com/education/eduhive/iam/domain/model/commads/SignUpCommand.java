package com.education.eduhive.iam.domain.model.commads;

import com.education.eduhive.iam.domain.model.entities.Role;
import com.education.eduhive.iam.domain.model.valueobjects.Roles;

import java.util.List;

public record SignUpCommand(String email, String password, List<Roles> roles) {

    public SignUpCommand {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be null or blank");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password cannot be null or blank");
        }
        if (roles == null || roles.isEmpty()) {
            throw new IllegalArgumentException("Roles cannot be null or empty");
        }
    }
}
