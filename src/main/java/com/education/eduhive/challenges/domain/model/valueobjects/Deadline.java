package com.education.eduhive.challenges.domain.model.valueobjects;

import java.util.Date;

public record Deadline(Date deadline) {
    public Deadline {
        if (deadline == null || deadline.before(new Date())) {
            throw new IllegalArgumentException("Deadline cannot be null or in the past");
        }
    }
}
