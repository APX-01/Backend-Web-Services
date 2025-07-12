package com.education.eduhive.submissions.application.internal.queryservices;

import com.education.eduhive.challenges.infrastructure.persistence.jpa.repositories.ChallengeRepository;
import com.education.eduhive.iam.infrastructure.persistence.jpa.repositories.UserRepository;
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
    private final ChallengeRepository challengeRepository;
    private final UserRepository userRepository;


    public SubmissionQueryServiceImpl(SubmissionRepository submissionRepository,ChallengeRepository challengeRepository, UserRepository userRepository) {
        this.submissionRepository = submissionRepository;
        this.challengeRepository = challengeRepository;
        this.userRepository = userRepository;
    }



    @Override
    public Optional<Submission> handle(GetSubmissionByIdQuery query, Long userId) {
        // 1. Validar Submission
        Optional<Submission> submissionOptional = submissionRepository.findById(query.submissionId());
        if (submissionOptional.isEmpty()) {
            throw new IllegalArgumentException("Submission with ID " + query.submissionId() + " not found");
        }

        // 2. Validar Usuario
        var userOptional = userRepository.findById(userId);
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User with ID " + userId + " not found");
        }

        // 3. Validar Challenge del Submission
        Long submissionChallengeId = submissionOptional.get().getChallengeId().challengeId();
        var challengeOptional = challengeRepository.findById(submissionChallengeId);
        if (challengeOptional.isEmpty()) {
            throw new IllegalArgumentException("Challenge with ID " + submissionChallengeId + " not found");
        }

        Long challengeGroupId = challengeOptional.get().getGroupId().groupId();

        // 4. Validar que el usuario pertenezca al grupo del Challenge
        boolean belongsToGroup = userOptional.get().getProfilesInGroups().stream()
                .anyMatch(profile -> profile.getGroupId().equals(challengeGroupId));

        if (!belongsToGroup) {
            throw new IllegalArgumentException("User does not belong to the group associated with this Submission's Challenge");
        }

        return submissionOptional;
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

    @Override
    public List<Submission> handle(GetSubmissionsByStudentIdAndGroupIdQuery query) {
        StudentId studentId = new StudentId(query.studentId());
        List<Submission> allSubmissions = submissionRepository.findByStudentId(studentId);

        return allSubmissions.stream()
                .filter(submission -> {
                    Long challengeId = submission.getChallengeId().challengeId();
                    return challengeRepository.findById(challengeId)
                            .map(challenge -> challenge.getGroupId().groupId().equals(query.groupId()))
                            .orElse(false);
                })
                .toList();
    }

    @Override
    public List<Submission> handle(GetSubmissionsByGroupIdQuery query) {
        List<Submission> allSubmissions = submissionRepository.findAll();

        return allSubmissions.stream()
                .filter(submission -> {
                    Long challengeId = submission.getChallengeId().challengeId();
                    return challengeRepository.findById(challengeId)
                            .map(challenge -> challenge.getGroupId().groupId().equals(query.groupId()))
                            .orElse(false);
                })
                .toList();
    }
}
