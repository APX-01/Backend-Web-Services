package com.education.eduhive.submissions.interfaces.rest.transform;

import com.education.eduhive.submissions.domain.model.aggregates.Submission;
import com.education.eduhive.submissions.interfaces.rest.resources.SubmissionResource;

public class SubmissionResourceFromEntityAssembler {
    public static SubmissionResource toResourceFromEntity(Submission submissionEntity){
        return new SubmissionResource(
                submissionEntity.getId(),
                submissionEntity.getChallengeId().challengeId(),
                submissionEntity.getStudentId().studentId(),
                submissionEntity.getContent().content(),
                submissionEntity.getScore().score(),
                submissionEntity.getImageUrl(),
                submissionEntity.getState().name()
        );
    }
}
