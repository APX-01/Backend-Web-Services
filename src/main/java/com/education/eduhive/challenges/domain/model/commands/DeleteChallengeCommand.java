package com.education.eduhive.challenges.domain.model.commands;

public record DeleteChallengeCommand(Long challengeId) {

    public DeleteChallengeCommand {
        if (challengeId == null || challengeId <= 0)
            throw new IllegalArgumentException("Challenge ID is required and must be greater than zero!");
    }
}
