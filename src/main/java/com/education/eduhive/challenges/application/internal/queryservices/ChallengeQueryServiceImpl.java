package com.education.eduhive.challenges.application.internal.queryservices;

import com.education.eduhive.challenges.domain.model.aggregates.Challenge;
import com.education.eduhive.challenges.domain.model.queries.GetAllChallengesQuery;
import com.education.eduhive.challenges.domain.model.queries.GetChallengeByIdQuery;
import com.education.eduhive.challenges.domain.model.queries.GetChallengesByGroupIdQuery;
import com.education.eduhive.challenges.domain.model.valueobjects.GroupId;
import com.education.eduhive.challenges.domain.services.ChallengeQueryService;
import com.education.eduhive.challenges.infrastructure.persistence.jpa.repositories.ChallengeRepository;
import com.education.eduhive.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ChallengeQueryServiceImpl implements ChallengeQueryService {

    final private ChallengeRepository challengeRepository;
    final private UserRepository userRepository;

    public ChallengeQueryServiceImpl(ChallengeRepository challengeRepository, UserRepository userRepository) {
        this.challengeRepository = challengeRepository;
        this.userRepository = userRepository;
    }


    @Override
    public Optional<Challenge> handle(GetChallengeByIdQuery query,Long userId) {
        // 1. Buscar el Challenge
        Optional<Challenge> challengeOpt = challengeRepository.findById(query.challengeId());
        if (challengeOpt.isEmpty()) {
            return Optional.empty();
        }

        // 2. Buscar el Usuario
        var userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User with ID " + userId + " not found");
        }

        // 3. Obtener el GroupId del Challenge
        Long challengeGroupId = challengeOpt.get().getGroupId().groupId();

        // 4. Validar pertenencia del usuario al grupo
        boolean belongsToGroup = userOpt.get().getProfilesInGroups().stream()
                .anyMatch(profile -> profile.getGroupId().equals(challengeGroupId));

        if (!belongsToGroup) {
            // Usuario no tiene acceso al Challenge porque no pertenece al grupo
            throw new IllegalArgumentException("User with ID " + userId + " does not belong to the group of this challenge");
        }

        return challengeOpt;
    }

    @Override
    public List<Challenge> handle(GetAllChallengesQuery getAllChallengesQuery) {
        return challengeRepository.findAll();
    }

    @Override
    public List<Challenge> handle(GetChallengesByGroupIdQuery query,Long userId) {
        Long groupId = query.groupId();

        // 1. Verificar que el usuario existe
        var userOpt = userRepository.findById(userId);
        if (userOpt.isEmpty()) {
            throw new IllegalArgumentException("User with ID " + userId + " not found");
        }

        // 2. Verificar que pertenece al grupo
        boolean belongsToGroup = userOpt.get().getProfilesInGroups().stream()
                .anyMatch(profile -> profile.getGroupId().equals(groupId));

        if (!belongsToGroup) {
            // Usuario no tiene acceso a Challenges de este grupo
            return List.of();
        }

        // 3. Si pertenece, devolver los Challenges
        return challengeRepository.findByGroupId(new GroupId(groupId));
    }
}
