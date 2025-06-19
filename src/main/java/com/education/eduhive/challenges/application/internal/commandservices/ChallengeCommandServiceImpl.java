package com.education.eduhive.challenges.application.internal.commandservices;

import com.education.eduhive.challenges.domain.model.aggregates.Challenge;
import com.education.eduhive.challenges.domain.model.commands.CreateChallengeCommand;
import com.education.eduhive.challenges.domain.model.commands.DeleteChallengeCommand;
import com.education.eduhive.challenges.domain.model.commands.UpdateChallengeCommand;
import com.education.eduhive.challenges.domain.model.valueobjects.Title;
import com.education.eduhive.challenges.domain.services.ChallengeCommandService;
import com.education.eduhive.challenges.infrastructure.persistence.jpa.repositories.ChallengeRepository;
import com.education.eduhive.groups.infrastructure.persistence.jpa.repositories.GroupRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ChallengeCommandServiceImpl implements ChallengeCommandService {

    private final ChallengeRepository challengeRepository;
    private final GroupRepository groupRepository;

    public ChallengeCommandServiceImpl(ChallengeRepository challengeRepository,GroupRepository groupRepository) {
        this.challengeRepository = challengeRepository;
        this.groupRepository = groupRepository;
    }



    @Override
    public Long handle(CreateChallengeCommand createChallengeCommand) {

        // Validación cruzada entre BCs
        if (!groupRepository.existsById(createChallengeCommand.groupId())){
            throw new IllegalArgumentException("Group not found: ");
        }


        if (challengeRepository.existsByTitle(new Title(createChallengeCommand.title()))){
            throw new IllegalArgumentException("Title already exists");
        }

        var challenge= new Challenge(createChallengeCommand);
        try{
            challengeRepository.save(challenge);
            return challenge.getId();
        }catch (Exception e){
            throw new IllegalArgumentException("Failed to create challenge: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Challenge> handle(UpdateChallengeCommand updateChallengeCommand) {
        var challenge = challengeRepository.findById(updateChallengeCommand.challengeId());
        if (challenge.isEmpty()) {
            throw new IllegalArgumentException("Challenge not found: " + updateChallengeCommand.challengeId());
        }
        var challengeToUpdate = challenge.get();
        try{
            var updatedChallenge= challengeRepository.save(challengeToUpdate
                    .updateInformation(
                            updateChallengeCommand.title(),
                            updateChallengeCommand.description(),
                            updateChallengeCommand.groupId(),
                            updateChallengeCommand.deadline(),
                            updateChallengeCommand.imageUrl()
                    ));
            return Optional.of(updatedChallenge);
        }catch (Exception e){
            throw new IllegalArgumentException("Failed to update challenge: " + e.getMessage(), e);
        }

    }

    @Override
    public void handle(DeleteChallengeCommand deleteChallengeCommand) {
        if (!challengeRepository.existsById(deleteChallengeCommand.challengeId())) {
            throw new IllegalArgumentException("Challenge not found: " + deleteChallengeCommand.challengeId());
        }

        try{
            challengeRepository.deleteById(deleteChallengeCommand.challengeId());
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to delete challenge: " + e.getMessage(), e);
        }

    }
}
