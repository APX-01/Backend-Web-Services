package com.education.eduhive.challenges.interfaces.rest.transform;

import com.education.eduhive.challenges.domain.model.aggregates.Challenge;
import com.education.eduhive.challenges.domain.model.commands.UpdateChallengeCommand;
import com.education.eduhive.challenges.interfaces.rest.resource.UpdateChallengeResource;

public class UpdateChallengeCommandFromResourceAssembler {
    public static UpdateChallengeCommand toCommandFromResource(Long challengeId,UpdateChallengeResource updateChallengeResource){
        return new UpdateChallengeCommand(
                challengeId,
                updateChallengeResource.title(),
                updateChallengeResource.description(),
                updateChallengeResource.groupId(),
                updateChallengeResource.deadline(),
                updateChallengeResource.imageUrl()
        );
    }
}