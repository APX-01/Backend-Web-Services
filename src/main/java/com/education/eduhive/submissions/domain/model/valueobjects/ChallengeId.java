package com.education.eduhive.submissions.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record ChallengeId(Long challengeId) {

    public ChallengeId {
        if (challengeId == null || challengeId < 0) {
            throw new IllegalArgumentException("ChallengeId must be greater than 0");
        }
    }
}
