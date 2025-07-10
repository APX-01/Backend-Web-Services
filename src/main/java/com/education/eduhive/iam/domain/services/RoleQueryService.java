package com.education.eduhive.iam.domain.services;

import com.education.eduhive.iam.domain.model.entities.Role;
import com.education.eduhive.iam.domain.model.queries.GetAllRolesQuery;
import com.education.eduhive.iam.domain.model.queries.GetRoleByNameQuery;

import java.util.List;
import java.util.Optional;

public interface RoleQueryService {

    List<Role> handle(GetAllRolesQuery getAllRolesQuery);

    Optional<Role> handle(GetRoleByNameQuery getRoleByNameQuery);
}
