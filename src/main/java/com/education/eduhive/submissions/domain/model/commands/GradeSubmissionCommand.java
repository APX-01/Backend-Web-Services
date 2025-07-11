package com.education.eduhive.submissions.domain.model.commands;

public record GradeSubmissionCommand(
        Long submissionId,
        int score
) {}
