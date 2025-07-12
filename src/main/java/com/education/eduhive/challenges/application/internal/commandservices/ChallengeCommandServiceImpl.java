package com.education.eduhive.challenges.application.internal.commandservices;

import com.education.eduhive.challenges.domain.model.aggregates.Challenge;
import com.education.eduhive.challenges.domain.model.commands.CreateChallengeCommand;
import com.education.eduhive.challenges.domain.model.commands.DeleteChallengeCommand;
import com.education.eduhive.challenges.domain.model.commands.UpdateChallengeCommand;
import com.education.eduhive.challenges.domain.model.valueobjects.GroupId;
import com.education.eduhive.challenges.domain.model.valueobjects.Title;
import com.education.eduhive.challenges.domain.services.ChallengeCommandService;
import com.education.eduhive.challenges.infrastructure.persistence.jpa.repositories.ChallengeRepository;
import com.education.eduhive.groups.infrastructure.persistence.jpa.repositories.GroupRepository;
import com.education.eduhive.iam.infrastructure.authorization.sfs.model.UserDetailsImpl;
import com.education.eduhive.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ChallengeCommandServiceImpl implements ChallengeCommandService {

    private final ChallengeRepository challengeRepository;
    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    public ChallengeCommandServiceImpl(ChallengeRepository challengeRepository,GroupRepository groupRepository, UserRepository userRepository) {
        this.challengeRepository = challengeRepository;
        this.groupRepository = groupRepository;
        this.userRepository=userRepository;
    }

    @Override
    public Long handle(CreateChallengeCommand createChallengeCommand, Long userId) {

        // Validación: grupo debe existir
        if (!groupRepository.existsById(createChallengeCommand.groupId())){
            throw new IllegalArgumentException("Group not found: "+ createChallengeCommand.groupId());
        }

        // Validar usuario pertenece al grupo
        var userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User not found: " + userId);
        }
        var user = userOptional.get();

        boolean isMember = user.getProfilesInGroups().stream()
                .anyMatch(p -> p.getGroupId().equals(createChallengeCommand.groupId()));

        if (!isMember) {
            throw new IllegalStateException("User does not belong to the group");
        }

        // Validar título único en el grupo
        if (challengeRepository.existsByTitleAndGroupId(new Title(createChallengeCommand.title()), new GroupId(createChallengeCommand.groupId()))) {
            throw new IllegalArgumentException("A challenge with this title already exists in the group");
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
    public Optional<Challenge> handle(UpdateChallengeCommand updateChallengeCommand, Long userId) {
        var challenge = challengeRepository.findById(updateChallengeCommand.challengeId());
        if (challenge.isEmpty()) {
            throw new IllegalArgumentException("Challenge not found: " + updateChallengeCommand.challengeId());
        }

        // Validación cruzada entre BCs
        if (!groupRepository.existsById(updateChallengeCommand.groupId())) {
            throw new IllegalArgumentException("Group not found: " + updateChallengeCommand.groupId());
        }

        // Validar usuario pertenece al grupo
        var userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User not found: " + userId);
        }
        var user = userOptional.get();

        boolean isMember = user.getProfilesInGroups().stream()
                .anyMatch(p -> p.getGroupId().equals(updateChallengeCommand.groupId()));

        if (!isMember) {
            throw new IllegalStateException("User does not belong to the group");
        }


        // Validar que no exista otro challenge en el mismo grupo con el mismo título
        var title = new Title(updateChallengeCommand.title());
        var groupId = new GroupId(updateChallengeCommand.groupId());
        var existingChallengeWithSameTitle = challengeRepository.findByTitleAndGroupId(title, groupId);

        if (existingChallengeWithSameTitle.isPresent() &&
                !existingChallengeWithSameTitle.get().getId().equals(updateChallengeCommand.challengeId())) {
            throw new IllegalArgumentException("A challenge with this title already exists in the group");
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
