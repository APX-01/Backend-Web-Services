package com.education.eduhive.groups.domain.model.commands;

public record JoinGroupByCodeCommand(
        Long userId,
        String joinKey
) {
    public JoinGroupByCodeCommand {
        if (userId == null || userId <= 0)
            throw new IllegalArgumentException("UserId must be greater than 0");
        if (joinKey == null || joinKey.isBlank())
            throw new IllegalArgumentException("Join key must not be blank");
    }
}
