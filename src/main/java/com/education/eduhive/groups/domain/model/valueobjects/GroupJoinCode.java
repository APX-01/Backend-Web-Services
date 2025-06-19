package com.education.eduhive.groups.domain.model.valueobjects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Date;

@Embeddable
public record GroupJoinCode(
        @Column(name = "join_key")
        String key,
        Date expiration
) {
    public GroupJoinCode {

        if (key == null || key.isEmpty()) {
            throw new IllegalArgumentException("Key cannot be null or empty");
        }
    }
}
