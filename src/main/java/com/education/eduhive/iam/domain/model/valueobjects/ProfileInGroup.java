package com.education.eduhive.iam.domain.model.valueobjects;

import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Embeddable
public class ProfileInGroup {

    private Long groupId;

    @Setter
    private int score;

    protected ProfileInGroup() {}

    public ProfileInGroup(Long groupId, Integer score) {
        this.groupId = groupId;
        this.score = score;
    }


}
