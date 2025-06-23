package com.education.eduhive.submissions.domain.model.queries;

public record GetSubmissionsByStudentIdAndGroupIdQuery(Long studentId, Long groupId) {

    public GetSubmissionsByStudentIdAndGroupIdQuery {
        if (studentId == null || studentId <= 0) {
            throw new IllegalArgumentException("Student ID must be a positive number");
        }
        if (groupId == null || groupId <= 0) {
            throw new IllegalArgumentException("Group ID must be a positive number");
        }
    }
}
