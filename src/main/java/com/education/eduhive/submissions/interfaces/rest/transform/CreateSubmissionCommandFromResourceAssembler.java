package com.education.eduhive.submissions.interfaces.rest.transform;

import com.education.eduhive.submissions.domain.model.commands.CreateSubmissionCommand;
import com.education.eduhive.submissions.interfaces.rest.resources.CreateSubmissionResource;

public class CreateSubmissionCommandFromResourceAssembler {
    public static CreateSubmissionCommand toCommandFromResource(CreateSubmissionResource createSubmissionResource,Long studentId) {
        return new CreateSubmissionCommand(
                createSubmissionResource.challengeId(),
                studentId,
                createSubmissionResource.content(),
                createSubmissionResource.imageUrl()
        );
    }
}