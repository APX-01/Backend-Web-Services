package com.education.eduhive.submissions.interfaces.rest.transform;

import com.education.eduhive.submissions.domain.model.commands.GradeSubmissionCommand;
import com.education.eduhive.submissions.interfaces.rest.resources.GradeSubmissionResource;

public class GradeSubmissionCommandFromResourceAssembler {
    public static GradeSubmissionCommand toCommandFromResource(Long submissionId, GradeSubmissionResource resource) {
        return new GradeSubmissionCommand(submissionId, resource.getScore());
    }
}
