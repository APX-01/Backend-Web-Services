package com.education.eduhive.submissions.application.internal.commandservices;

import com.education.eduhive.challenges.infrastructure.persistence.jpa.repositories.ChallengeRepository;
import com.education.eduhive.iam.domain.model.valueobjects.Roles;
import com.education.eduhive.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import com.education.eduhive.submissions.domain.model.aggregates.Submission;
import com.education.eduhive.submissions.domain.model.commands.CreateSubmissionCommand;
import com.education.eduhive.submissions.domain.model.commands.DeleteSubmissionCommand;
import com.education.eduhive.submissions.domain.model.commands.UpdateSubmissionCommand;
import com.education.eduhive.submissions.domain.services.SubmissionCommandService;
import com.education.eduhive.submissions.infrastructure.persistence.jpa.respositories.SubmissionRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SubmissionCommandServiceImpl implements SubmissionCommandService {

    private final SubmissionRepository submissionRepository;
    private final ChallengeRepository challengeRepository;
    private final UserRepository userRepository;

    public SubmissionCommandServiceImpl(SubmissionRepository submissionRepository,ChallengeRepository challengeRepository,UserRepository userRepository) {
        this.submissionRepository = submissionRepository;
        this.challengeRepository = challengeRepository;
        this.userRepository= userRepository;
    }

    @Override
    public Long handle(CreateSubmissionCommand createSubmissionCommand) {

        // 1. Verificar que el Challenge existe
        var challengeOptional = challengeRepository.findById(createSubmissionCommand.challengeId());
        if (challengeOptional.isEmpty()) {
            throw new IllegalArgumentException("Challenge with ID " + createSubmissionCommand.challengeId() + " not found");
        }
        var challenge = challengeOptional.get();

        // 2. Verificar que el usuario existe y sea estudiante
        var optionalUser = userRepository.findById(createSubmissionCommand.studentId());
        if (optionalUser.isEmpty()) {
            throw new IllegalArgumentException("Student no encontrado");
        }
        var user = optionalUser.get();
        if (user.getRoles().stream().noneMatch(role -> role.getName().equals(Roles.ROLE_STUDENT))) {
            throw new IllegalStateException("Solo un usuario con rol STUDENT puede crear un submission");
        }

        // 3. Obtener el grupo al que pertenece el challenge
        Long challengeGroupId = challenge.getGroupId().groupId(); // ⚠️ usa el value object GroupId
        boolean belongsToGroup = user.getProfilesInGroups().stream()
                .anyMatch(profile -> profile.getGroupId().equals(challengeGroupId));

        if (!belongsToGroup) {
            throw new IllegalArgumentException("Student does not belong to the group of this challenge");
        }

        // 4. Crear la Submission
        var submission= new Submission(createSubmissionCommand);
        try {
            submissionRepository.save(submission);
            return submission.getId();

        } catch (Exception e) {
            // Aquí podrías manejar excepciones específicas o registrar el error
            throw new RuntimeException("Error creating submission: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<Submission> handle(UpdateSubmissionCommand updateSubmissionCommand) {
        // 1. Validar que el challenge exista
        var challengeOptional = challengeRepository.findById(updateSubmissionCommand.challengeId());
        if (challengeOptional.isEmpty()) {
            throw new IllegalArgumentException("Challenge with ID " + updateSubmissionCommand.challengeId() + " not found");
        }
        var challenge = challengeOptional.get();

        // 2. Validar que el estudiante exista y sea de rol STUDENT
        var userOptional = userRepository.findById(updateSubmissionCommand.studentId());
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("Student with ID " + updateSubmissionCommand.studentId() + " not found");
        }

        var student = userOptional.get();
        if (!student.getRoles().equals(Roles.ROLE_STUDENT)) {
            throw new IllegalArgumentException("Only students can update submissions");
        }

        // 3. Validar que el estudiante pertenece al grupo del challenge
        Long challengeGroupId = challenge.getGroupId().groupId(); // es value object
        boolean belongsToGroup = student.getProfilesInGroups().stream()
                .anyMatch(p -> p.getGroupId().equals(challengeGroupId));

        if (!belongsToGroup) {
            throw new IllegalArgumentException("Student does not belong to the group of this challenge");
        }

        // 4. Verificar que el submission exista
        var submissionOptional = submissionRepository.findById(updateSubmissionCommand.submissionId());
        if (submissionOptional.isEmpty()) {
            throw new IllegalArgumentException("Submission with ID " + updateSubmissionCommand.submissionId() + " not found");
        }

        var submissionToUpdate = submissionOptional.get();

        // 5. Actualizar y guardar
        try {
            var updatedSubmission = submissionRepository.save(submissionToUpdate
                    .updateSubmission(
                            updateSubmissionCommand.challengeId(),
                            updateSubmissionCommand.studentId(),
                            updateSubmissionCommand.content(),
                            updateSubmissionCommand.score(),
                            updateSubmissionCommand.imageUrl()
                    ));
            return Optional.of(updatedSubmission);
        } catch (Exception e) {
            throw new RuntimeException("Error updating submission: " + e.getMessage(), e);
        }
    }

    @Override
    public void handle(DeleteSubmissionCommand deleteSubmissionCommand) {
        if (!submissionRepository.existsById(deleteSubmissionCommand.submissionId())) {
            throw new IllegalArgumentException("Submission with ID " + deleteSubmissionCommand.submissionId() + " not found");
        }
        try{
            submissionRepository.deleteById(deleteSubmissionCommand.submissionId());
        }catch (Exception e) {
            throw new RuntimeException("Error deleting submission: " + e.getMessage(), e);
        }
    }


}
