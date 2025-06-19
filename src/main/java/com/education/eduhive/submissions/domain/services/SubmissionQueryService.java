package com.education.eduhive.submissions.domain.services;

import com.education.eduhive.submissions.domain.model.aggregates.Submission;
import com.education.eduhive.submissions.domain.model.queries.GetAllSubmissionsQuery;
import com.education.eduhive.submissions.domain.model.queries.GetSubmissionByIdQuery;
import com.education.eduhive.submissions.domain.model.queries.GetSubmissionsByChallengeIdQuery;

import java.util.List;
import java.util.Optional;

public interface SubmissionQueryService {

    Optional<Submission> handle(GetSubmissionByIdQuery getSubmissionByIdQuery);

    List<Submission> handle(GetAllSubmissionsQuery getAllSubmissionsQuery);

    List<Submission> handle(GetSubmissionsByChallengeIdQuery getSubmissionsByChallengeIdQuery);

}
