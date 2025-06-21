package com.education.eduhive.challenges.infrastructure.persistence.jpa.repositories;

import com.education.eduhive.challenges.domain.model.aggregates.Challenge;
import com.education.eduhive.challenges.domain.model.valueobjects.GroupId;
import com.education.eduhive.challenges.domain.model.valueobjects.Title;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChallengeRepository extends JpaRepository<Challenge, Long> {

    boolean existsByTitle(Title title);

    List<Challenge> findByGroupId(GroupId groupId);

    boolean existsByTitleAndGroupId(Title title, GroupId groupId);

    Optional<Challenge> findByTitleAndGroupId(Title title, GroupId groupId);
}
