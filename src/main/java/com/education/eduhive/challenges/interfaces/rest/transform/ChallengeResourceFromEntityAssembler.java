package com.education.eduhive.challenges.interfaces.rest.transform;

import com.education.eduhive.challenges.domain.model.aggregates.Challenge;
import com.education.eduhive.challenges.interfaces.rest.resource.ChallengeResource;

public class ChallengeResourceFromEntityAssembler {
    public static ChallengeResource toResourceFromEntity(Challenge challengeEntity) {
        return new ChallengeResource(
                challengeEntity.getId(),
                challengeEntity.getTitle().title(),
                challengeEntity.getDescription().description(),
                challengeEntity.getGroupId().groupId(),
                challengeEntity.getDeadline().deadline(),
                challengeEntity.getImageUrl()
        );
    }
}
