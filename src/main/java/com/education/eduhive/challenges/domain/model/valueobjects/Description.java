package com.education.eduhive.challenges.domain.model.valueobjects;

public record Description(String description) {
    public Description{
        if (description == null || description.isBlank()){
            throw new IllegalArgumentException("Description cannot be null or blank");
        }
    }
}
