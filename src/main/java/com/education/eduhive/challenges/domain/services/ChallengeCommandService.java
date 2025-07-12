package com.education.eduhive.challenges.domain.services;

import com.education.eduhive.challenges.domain.model.aggregates.Challenge;
import com.education.eduhive.challenges.domain.model.commands.CreateChallengeCommand;
import com.education.eduhive.challenges.domain.model.commands.DeleteChallengeCommand;
import com.education.eduhive.challenges.domain.model.commands.UpdateChallengeCommand;

import java.util.Optional;

public interface ChallengeCommandService {

    Long handle(CreateChallengeCommand createChallengeCommand, Long userid);

    Optional<Challenge> handle(UpdateChallengeCommand updateChallengeCommand, Long userid);

    void handle(DeleteChallengeCommand deleteChallengeCommand);
}
