package com.education.eduhive.iam.domain.model.queries;

public record GetProfilesInGroupsByGroupIdAndStudentIdQuery(Long groupId, Long studentId) {

    public GetProfilesInGroupsByGroupIdAndStudentIdQuery {
        if (groupId == null || groupId <= 0) {
            throw new IllegalArgumentException("Group ID must be a positive number");
        }
        if (studentId == null || studentId <= 0) {
            throw new IllegalArgumentException("Student ID must be a positive number");
        }
    }
}
