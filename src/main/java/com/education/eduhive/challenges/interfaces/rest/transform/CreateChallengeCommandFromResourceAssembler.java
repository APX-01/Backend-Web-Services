package com.education.eduhive.challenges.interfaces.rest.transform;

import com.education.eduhive.challenges.domain.model.commands.CreateChallengeCommand;
import com.education.eduhive.challenges.interfaces.rest.resource.CreateChallengeResource;

public class CreateChallengeCommandFromResourceAssembler {
    public static CreateChallengeCommand toCommandFromResource(CreateChallengeResource createChallengeResource){
        return new CreateChallengeCommand(
                createChallengeResource.title(),
                createChallengeResource.description(),
                createChallengeResource.groupId(),
                createChallengeResource.deadline(),
                createChallengeResource.imageUrl()
        );
    }
}
