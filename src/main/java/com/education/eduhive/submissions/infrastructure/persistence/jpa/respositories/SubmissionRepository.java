package com.education.eduhive.submissions.infrastructure.persistence.jpa.respositories;

import com.education.eduhive.submissions.domain.model.aggregates.Submission;
import com.education.eduhive.submissions.domain.model.valueobjects.ChallengeId;
import com.education.eduhive.submissions.domain.model.valueobjects.StudentId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Long> {

    // Additional query methods can be defined here if needed
    List<Submission> findByChallengeId(ChallengeId challengeId);

    List<Submission> findByStudentId(StudentId studentId);

    List<Submission> findByStudentIdAndChallengeId(StudentId studentId, ChallengeId challengeId);
}
