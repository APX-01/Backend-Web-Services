package com.education.eduhive.submissions.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record Score(int score) {
    public Score {
        if (score < 0 || score > 20) {
            throw new IllegalArgumentException("Score must be between 0 and 20");
        }
    }
}
