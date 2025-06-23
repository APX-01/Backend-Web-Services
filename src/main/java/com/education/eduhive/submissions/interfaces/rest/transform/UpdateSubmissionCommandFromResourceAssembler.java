package com.education.eduhive.submissions.interfaces.rest.transform;

import com.education.eduhive.submissions.domain.model.commands.UpdateSubmissionCommand;
import com.education.eduhive.submissions.interfaces.rest.resources.UpdateSubmissionResource;

public class UpdateSubmissionCommandFromResourceAssembler {
    public static UpdateSubmissionCommand toCommandFromResource(Long submissionId, UpdateSubmissionResource updateSubmissionResource){
        return new UpdateSubmissionCommand(
                submissionId,
                updateSubmissionResource.challengeId(),
                updateSubmissionResource.studentId(),
                updateSubmissionResource.content(),
                updateSubmissionResource.score(),
                updateSubmissionResource.imageUrl()
        );
    }
}
