package com.education.eduhive.challenges.domain.model.valueobjects;

public record Title(String title) {

    public Title {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title cannot be null or blank");
        }
    }

}
