package com.education.eduhive.challenges.domain.services;

import com.education.eduhive.challenges.domain.model.aggregates.Challenge;
import com.education.eduhive.challenges.domain.model.queries.GetAllChallengesQuery;
import com.education.eduhive.challenges.domain.model.queries.GetChallengeByIdQuery;
import com.education.eduhive.challenges.domain.model.queries.GetChallengesByGroupIdQuery;

import java.util.List;
import java.util.Optional;

public interface ChallengeQueryService {

    Optional<Challenge> handle(GetChallengeByIdQuery getChallengeByIdQuery);

    List<Challenge> handle(GetAllChallengesQuery getAllChallengesQuery);

    List<Challenge> handle(GetChallengesByGroupIdQuery getChallengesByGroupIdQuery);
}
