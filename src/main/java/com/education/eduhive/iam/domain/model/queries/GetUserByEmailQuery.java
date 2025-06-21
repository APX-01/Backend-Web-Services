package com.education.eduhive.iam.domain.model.queries;

public record GetUserByEmailQuery(String email) {

    public GetUserByEmailQuery {
        if (email == null || email.isBlank()) {
            throw new IllegalArgumentException("Email cannot be null or blank");
        }
        if (!email.matches("^.+@.+$")) {
            throw new IllegalArgumentException("Formato de email inválido");
        }
    }
}
