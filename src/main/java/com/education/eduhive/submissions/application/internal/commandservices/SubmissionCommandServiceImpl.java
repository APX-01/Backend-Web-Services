package com.education.eduhive.submissions.application.internal.commandservices;

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

    public SubmissionCommandServiceImpl(SubmissionRepository submissionRepository) {
        this.submissionRepository = submissionRepository;
    }

    @Override
    public Long handle(CreateSubmissionCommand createSubmissionCommand) {
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
        var submission = submissionRepository.findById(updateSubmissionCommand.submissionId());
        if (submission.isEmpty()) {
            throw new IllegalArgumentException("Submission with ID " + updateSubmissionCommand.submissionId() + " not found");
        }
        var submissionToUpdate = submission.get();
        try{
            var updatedSubmission= submissionRepository.save(submissionToUpdate
                    .updateSubmission(
                            updateSubmissionCommand.challengeId(),
                            updateSubmissionCommand.studentId(),
                            updateSubmissionCommand.content(),
                            updateSubmissionCommand.score(),
                            updateSubmissionCommand.imageUrl()));
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
