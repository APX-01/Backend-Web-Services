package com.education.eduhive.submissions.interfaces.rest.transform;

import com.education.eduhive.submissions.domain.model.commands.CreateSubmissionCommand;
import com.education.eduhive.submissions.interfaces.rest.resources.CreateSubmissionResource;

public class CreateSubmissionCommandFromResourceAssembler {
    public static CreateSubmissionCommand toCommandFromResource(CreateSubmissionResource createSubmissionResource) {
        return new CreateSubmissionCommand(
                createSubmissionResource.challengeId(),
                createSubmissionResource.studentId(),
                createSubmissionResource.content(),
                createSubmissionResource.score(),
                createSubmissionResource.imageUrl()
        );
    }
}