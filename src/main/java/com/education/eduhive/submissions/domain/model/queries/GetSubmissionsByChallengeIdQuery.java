package com.education.eduhive.submissions.domain.model.queries;

public record GetSubmissionsByChallengeIdQuery(Long challengeId) {

    public GetSubmissionsByChallengeIdQuery {
        if (challengeId == null || challengeId <= 0) {
            throw new IllegalArgumentException("ChallengeId must be greater than 0");
        }
    }
}
