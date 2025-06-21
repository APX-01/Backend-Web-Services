package com.education.eduhive.submissions.domain.model.queries;

public record GetSubmissionsByStudentIdAndChallengeIdQuery(Long studentId, Long challengeId) {

    public GetSubmissionsByStudentIdAndChallengeIdQuery {
        if (studentId == null || studentId <= 0) {
            throw new IllegalArgumentException("Student ID must be greater than 0");
        }
        if (challengeId == null || challengeId <= 0) {
            throw new IllegalArgumentException("Challenge ID must be greater than 0");
        }
    }
}
