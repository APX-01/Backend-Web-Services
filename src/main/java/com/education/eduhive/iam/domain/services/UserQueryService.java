package com.education.eduhive.iam.domain.services;

import com.education.eduhive.iam.domain.model.aggregates.User;
import com.education.eduhive.iam.domain.model.queries.GetAllUsersQuery;
import com.education.eduhive.iam.domain.model.queries.GetUserByIdQuery;

import java.util.List;
import java.util.Optional;

public interface UserQueryService {
    Optional<User> handle(GetUserByIdQuery getUserByIdQuery);

    List<User> handle(GetAllUsersQuery getAllUsersQuery);


}
