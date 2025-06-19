package com.education.eduhive.iam.domain.model.queries;

public record GetTeacherByIdQuery(Long teacherId) {

    public GetTeacherByIdQuery {
        if (teacherId == null || teacherId <= 0) {
            throw new IllegalArgumentException("Teacher ID must be a positive number");
        }
    }
}
