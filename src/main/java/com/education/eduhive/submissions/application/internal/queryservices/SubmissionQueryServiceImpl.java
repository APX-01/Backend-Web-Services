package com.education.eduhive.submissions.application.internal.queryservices;

import com.education.eduhive.submissions.domain.model.aggregates.Submission;
import com.education.eduhive.submissions.domain.model.queries.*;
import com.education.eduhive.submissions.domain.model.valueobjects.ChallengeId;
import com.education.eduhive.submissions.domain.model.valueobjects.StudentId;
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

    @Override
    public List<Submission> handle(GetSubmissionsByChallengeIdQuery getSubmissionsByChallengeIdQuery) {
        //es de esta manera porque ChallengeId es un ValueObject
        return submissionRepository.findByChallengeId(new ChallengeId(getSubmissionsByChallengeIdQuery.challengeId()));
    }

    @Override
    public List<Submission> handle(GetSubmissionsByStudentIdQuery getSubmissionsByStudentIdQuery) {
        return submissionRepository.findByStudentId(new StudentId(getSubmissionsByStudentIdQuery.studentId()));
    }

    @Override
    public List<Submission> handle(GetSubmissionsByStudentIdAndChallengeIdQuery getSubmissionsByStudentIdAndChallengeIdQuery) {
        return submissionRepository.findByStudentIdAndChallengeId(new StudentId(getSubmissionsByStudentIdAndChallengeIdQuery.studentId()),new ChallengeId(getSubmissionsByStudentIdAndChallengeIdQuery.challengeId()));
    }
}
