package com.education.eduhive.iam.domain.model.queries;

public record GetUsersByGroupIdQuery(Long groupId) {

    public GetUsersByGroupIdQuery {
        if (groupId == null || groupId <= 0) {
            throw new IllegalArgumentException("Group ID must be a positive number");
        }
    }
}
