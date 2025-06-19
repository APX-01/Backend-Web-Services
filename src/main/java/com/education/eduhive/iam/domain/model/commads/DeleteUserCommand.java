package com.education.eduhive.iam.domain.model.commads;

public record DeleteUserCommand(Long userId) {

    public DeleteUserCommand {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("StudentId must be greater than 0");
        }
    }
}
