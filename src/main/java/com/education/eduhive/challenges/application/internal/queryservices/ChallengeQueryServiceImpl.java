package com.education.eduhive.challenges.application.internal.queryservices;

import com.education.eduhive.challenges.domain.model.aggregates.Challenge;
import com.education.eduhive.challenges.domain.model.queries.GetAllChallengesQuery;
import com.education.eduhive.challenges.domain.model.queries.GetChallengeByIdQuery;
import com.education.eduhive.challenges.domain.services.ChallengeQueryService;
import com.education.eduhive.challenges.infrastructure.persistence.jpa.repositories.ChallengeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChallengeQueryServiceImpl implements ChallengeQueryService {

    final private ChallengeRepository challengeRepository;

    public ChallengeQueryServiceImpl(ChallengeRepository challengeRepository) {
        this.challengeRepository = challengeRepository;
    }


    @Override
    public Optional<Challenge> handle(GetChallengeByIdQuery getChallengeByIdQuery) {
        return challengeRepository.findById(getChallengeByIdQuery.challengeId());

    }

    @Override
    public List<Challenge> handle(GetAllChallengesQuery getAllChallengesQuery) {
        return challengeRepository.findAll();
    }
}
