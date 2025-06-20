package com.education.eduhive.submissions.domain.model.queries;

public record GetSubmissionsByStudentIdQuery(Long studentId) {

    public GetSubmissionsByStudentIdQuery {
        if (studentId == null || studentId <= 0) {
            throw new IllegalArgumentException("Student ID must be greater than 0");
        }
    }
}
