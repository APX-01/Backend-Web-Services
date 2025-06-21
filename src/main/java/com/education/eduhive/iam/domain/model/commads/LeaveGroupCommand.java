package com.education.eduhive.iam.domain.model.commads;

public record LeaveGroupCommand(Long userId, Long groupId) {

    public LeaveGroupCommand {
        if (userId == null || userId <= 0) {
            throw new IllegalArgumentException("User ID must be a positive number");
        }
        if (groupId == null || groupId <= 0) {
            throw new IllegalArgumentException("Group ID must be a positive number");
        }
    }
}
