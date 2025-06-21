package com.education.eduhive.iam.domain.services;

import com.education.eduhive.iam.domain.model.aggregates.User;
import com.education.eduhive.iam.domain.model.commads.CreateUserCommand;
import com.education.eduhive.iam.domain.model.commads.DeleteUserCommand;
import com.education.eduhive.iam.domain.model.commads.LeaveGroupCommand;
import com.education.eduhive.iam.domain.model.commads.UpdateUserCommand;

import java.util.Optional;

public interface UserCommandService {

    Optional<User> handle(CreateUserCommand createUserCommand);

    Optional<User> handle(UpdateUserCommand updateUserCommand);

    void handle(DeleteUserCommand deleteUserCommand);

    Optional<User> handle(LeaveGroupCommand leaveGroupCommand);
}
