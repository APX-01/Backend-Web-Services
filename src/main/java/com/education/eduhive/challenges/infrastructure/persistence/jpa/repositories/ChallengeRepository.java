package com.education.eduhive.challenges.infrastructure.persistence.jpa.repositories;

import com.education.eduhive.challenges.domain.model.aggregates.Challenge;
import com.education.eduhive.challenges.domain.model.valueobjects.Title;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChallengeRepository extends JpaRepository<Challenge, Long> {

    boolean existsByTitle(Title title);
}
