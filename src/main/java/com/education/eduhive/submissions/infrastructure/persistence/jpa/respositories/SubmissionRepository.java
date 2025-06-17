package com.education.eduhive.submissions.infrastructure.persistence.jpa.respositories;

import com.education.eduhive.submissions.domain.model.aggregates.Submission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubmissionRepository extends JpaRepository<Submission, Long> {

    // Additional query methods can be defined here if needed

}
