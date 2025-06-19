package com.education.eduhive.challenges.domain.model.queries;

public record GetChallengesByGroupIdQuery(Long groupId) {

    public GetChallengesByGroupIdQuery {
        if (groupId == null || groupId <= 0) {
            throw new IllegalArgumentException("Group ID must be a positive number.");
        }
    }
}
