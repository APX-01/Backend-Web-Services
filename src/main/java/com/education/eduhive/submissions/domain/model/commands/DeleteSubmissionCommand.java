package com.education.eduhive.submissions.domain.model.commands;

public record DeleteSubmissionCommand(Long submissionId ) {

    public DeleteSubmissionCommand {
        if (submissionId == null || submissionId <= 0) {
            throw new IllegalArgumentException("SubmissionId must be greater than 0");
        }
    }
}
