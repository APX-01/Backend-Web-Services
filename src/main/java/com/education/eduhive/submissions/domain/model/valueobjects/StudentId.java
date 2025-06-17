package com.education.eduhive.submissions.domain.model.valueobjects;

import jakarta.persistence.Embeddable;

@Embeddable
public record StudentId(Long studentId) {

    public StudentId {
        if (studentId == null || studentId < 0) {
            throw new IllegalArgumentException("StudentId cannot be null or less than or equal to 0");
        }
    }
}
