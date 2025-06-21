package com.education.eduhive.iam.domain.services;

import com.education.eduhive.iam.domain.model.aggregates.User;
import com.education.eduhive.iam.domain.model.queries.*;
import com.education.eduhive.iam.domain.model.valueobjects.ProfileInGroup;

import java.util.List;
import java.util.Optional;

public interface UserQueryService {
    Optional<User> handle(GetUserByIdQuery getUserByIdQuery);

    List<User> handle(GetAllUsersQuery getAllUsersQuery);

    Optional<User> handle(GetUserByEmailAndPasswordQuery getUserByEmailAndPasswordQuery);

    Optional<User> handle(GetUserByEmailQuery getUserByEmailQuery);

    Optional<ProfileInGroup> handle(GetProfilesInGroupsByGroupIdAndStudentIdQuery getProfilesInGroupsByGroupIdAndStudentIdQuery);
}
