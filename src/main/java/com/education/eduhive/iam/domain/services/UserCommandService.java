package com.education.eduhive.iam.domain.services;

import com.education.eduhive.iam.domain.model.aggregates.User;
import com.education.eduhive.iam.domain.model.commads.*;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.Optional;

public interface UserCommandService {

//    Optional<User> handle(CreateUserCommand createUserCommand);

    Optional<User> handle(UpdateUserCommand updateUserCommand);

    void handle(DeleteUserCommand deleteUserCommand);

    Optional<User> handle(LeaveGroupCommand leaveGroupCommand);

    Optional<ImmutablePair<User, String>> handle(SignInCommand signInCommand);

    Optional<User> handle(SignUpCommand signUpCommand);
}
