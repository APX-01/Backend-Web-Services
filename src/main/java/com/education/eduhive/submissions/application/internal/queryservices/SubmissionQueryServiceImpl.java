package com.education.eduhive.submissions.application.internal.queryservices;

import com.education.eduhive.submissions.domain.model.aggregates.Submission;
import com.education.eduhive.submissions.domain.model.queries.GetAllSubmissionsQuery;
import com.education.eduhive.submissions.domain.model.queries.GetSubmissionByIdQuery;
import com.education.eduhive.submissions.domain.services.SubmissionQueryService;
import com.education.eduhive.submissions.infrastructure.persistence.jpa.respositories.SubmissionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubmissionQueryServiceImpl implements SubmissionQueryService {

    private final SubmissionRepository submissionRepository;

    public SubmissionQueryServiceImpl(SubmissionRepository submissionRepository) {
        this.submissionRepository = submissionRepository;
    }



    @Override
    public Optional<Submission> handle(GetSubmissionByIdQuery getSubmissionByIdQuery) {
        return submissionRepository.findById(getSubmissionByIdQuery.submissionId());
    }

    @Override
    public List<Submission> handle(GetAllSubmissionsQuery getAllSubmissionsQuery) {
        return submissionRepository.findAll();
    }
}
