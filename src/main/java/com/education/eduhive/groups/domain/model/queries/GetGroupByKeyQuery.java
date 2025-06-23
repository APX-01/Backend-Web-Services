package com.education.eduhive.groups.domain.model.queries;

public record GetGroupByKeyQuery(String key) {

    public GetGroupByKeyQuery {
        if (key == null || key.isBlank()) {
            throw new IllegalArgumentException("Key cannot be null or blank");
        }
    }

}
