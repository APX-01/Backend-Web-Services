package com.education.eduhive.groups.domain.model.queries;

public record GetGroupsByUserIdQuery(Long userId) {

    public GetGroupsByUserIdQuery {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("User ID must be a positive number");
        }
    }
}
