package com.education.eduhive.challenges.domain.model.queries;

public record GetChallengeByIdQuery(Long challengeId) {

    public GetChallengeByIdQuery {
        if (challengeId == null || challengeId <= 0)
            throw new IllegalArgumentException("Challenge ID must be a positive number.");
    }
}
