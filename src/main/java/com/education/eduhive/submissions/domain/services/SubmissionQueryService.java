package com.education.eduhive.submissions.domain.services;

import com.education.eduhive.submissions.domain.model.aggregates.Submission;
import com.education.eduhive.submissions.domain.model.queries.*;

import java.util.List;
import java.util.Optional;

public interface SubmissionQueryService {

    Optional<Submission> handle(GetSubmissionByIdQuery getSubmissionByIdQuery, Long userId);

    List<Submission> handle(GetAllSubmissionsQuery getAllSubmissionsQuery);

    List<Submission> handle(GetSubmissionsByChallengeIdQuery getSubmissionsByChallengeIdQuery);

    List<Submission> handle(GetSubmissionsByStudentIdQuery getSubmissionsByStudentIdQuery);

    List<Submission> handle(GetSubmissionsByStudentIdAndChallengeIdQuery getSubmissionsByStudentIdAndChallengeIdQuery);

    List<Submission> handle(GetSubmissionsByStudentIdAndGroupIdQuery getSubmissionsByStudentIdAndGroupIdQuery);
}
