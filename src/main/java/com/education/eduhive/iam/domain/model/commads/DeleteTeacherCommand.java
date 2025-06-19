package com.education.eduhive.iam.domain.model.commads;

public record DeleteTeacherCommand(Long teacherId) {

    public DeleteTeacherCommand {
        if (teacherId == null || teacherId <= 0) {
            throw new IllegalArgumentException("TeacherId must be greater than 0");
        }
    }
}
