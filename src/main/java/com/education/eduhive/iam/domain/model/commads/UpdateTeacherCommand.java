package com.education.eduhive.iam.domain.model.commads;

public record UpdateTeacherCommand(
        Long teacherId,
        String email,
        String firstName,
        String lastName,
        String password
) {
    public UpdateTeacherCommand {
        if (teacherId == null || teacherId <= 0) {
            throw new IllegalArgumentException("TeacherId must be greater than 0");
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
