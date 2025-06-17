package com.education.eduhive.challenges.domain.model.valueobjects;

public record GroupId(Long groupId) {
    public GroupId{
        if (groupId==null || groupId <= 0) {
            throw new IllegalArgumentException("The groupId cannot be null or less than 1");
        }
    }
}
