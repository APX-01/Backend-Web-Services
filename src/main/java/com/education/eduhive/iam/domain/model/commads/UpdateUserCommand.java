package com.education.eduhive.iam.domain.model.commads;

public record UpdateUserCommand(
        Long userId,
        String email,
        String firstName,
        String lastName,
        String password
) {

    public UpdateUserCommand {

        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("StudentId must be greater than 0");
        }
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


    }
}
