package com.education.eduhive.groups.interfaces.rest.resources;

public record JoinGroupResource(
        Long userId,
        String joinKey
) {
    public JoinGroupResource {
        if (userId == null || userId <= 0)
            throw new IllegalArgumentException("UserId must be greater than 0");
        if (joinKey == null || joinKey.isBlank())
            throw new IllegalArgumentException("Join key must not be blank");
    }
}