package com.education.eduhive.challenges.interfaces.rest.resource;

import java.util.Date;

public record UpdateChallengeResource(String title, String description, Long groupId, Date deadline, String imageUrl) {
    public UpdateChallengeResource {
        if (title == null || title.isBlank()) {
            throw new IllegalArgumentException("Title cannot be null or blank");
        }
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Description cannot be null or blank");
        }
        if (groupId == null || groupId <= 0) {
            throw new IllegalArgumentException("Group ID cannot be null or less than 1");
        }
        if (deadline == null || deadline.before(new Date())) {
            throw new IllegalArgumentException("Deadline cannot be null or in the past");
        }
    }
}
