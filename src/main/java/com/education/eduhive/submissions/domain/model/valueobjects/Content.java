package com.education.eduhive.submissions.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record Content(String content) {

    public Content {
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("Content cannot be null or blank");
        }
        if (content.length() > 500) {
            throw new IllegalArgumentException("Content cannot exceed 500 characters");
        }
    }
}
