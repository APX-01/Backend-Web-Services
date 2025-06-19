package com.education.eduhive.iam.domain.model.queries;

public record GetStudentByIdQuery(Long studentId) {
    public GetStudentByIdQuery {
        if (studentId == null || studentId <= 0) {
            throw new IllegalArgumentException("Student ID must be a positive number");
        }
    }
}
