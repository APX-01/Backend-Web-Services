package com.education.eduhive.iam.application.internal.queryservices;

import com.education.eduhive.iam.domain.model.aggregates.User;
import com.education.eduhive.iam.domain.model.queries.GetAllUsersQuery;
import com.education.eduhive.iam.domain.model.queries.GetUserByEmailAndPasswordQuery;
import com.education.eduhive.iam.domain.model.queries.GetUserByEmailQuery;
import com.education.eduhive.iam.domain.model.queries.GetUserByIdQuery;
import com.education.eduhive.iam.domain.services.UserQueryService;
import com.education.eduhive.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserQueryServiceImpl implements UserQueryService {

    private final UserRepository userRepository;

    public UserQueryServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Optional<User> handle(GetUserByIdQuery getUserByIdQuery) {
        return userRepository.findById(getUserByIdQuery.userId());
    }

    @Override
    public List<User> handle(GetAllUsersQuery getAllUsersQuery) {
        return userRepository.findAll();
    }

    @Override
    public Optional<User> handle(GetUserByEmailAndPasswordQuery getUserByEmailAndPasswordQuery) {
        return userRepository.findByEmailAndPassword(getUserByEmailAndPasswordQuery.email(), getUserByEmailAndPasswordQuery.password());
    }

    @Override
    public Optional<User> handle(GetUserByEmailQuery getUserByEmailQuery) {
        return userRepository.findByEmail(getUserByEmailQuery.email());
    }
}
