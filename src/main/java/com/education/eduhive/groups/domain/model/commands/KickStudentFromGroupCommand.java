package com.education.eduhive.groups.domain.model.commands;

public record KickStudentFromGroupCommand(Long studentId, Long groupId) {

    public KickStudentFromGroupCommand {
        if (studentId == null || studentId <= 0) {
            throw new IllegalArgumentException("Student ID must be greater than 0");
        }
        if (groupId == null || groupId <= 0) {
            throw new IllegalArgumentException("Group ID must be greater than 0");
        }
    }
}
