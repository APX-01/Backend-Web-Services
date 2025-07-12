package com.education.eduhive.iam.application.internal.queryservices;

import com.education.eduhive.iam.domain.model.entities.Role;
import com.education.eduhive.iam.domain.model.queries.GetAllRolesQuery;
import com.education.eduhive.iam.domain.model.queries.GetRoleByNameQuery;
import com.education.eduhive.iam.domain.services.RoleQueryService;
import com.education.eduhive.iam.infrastructure.persistence.jpa.repositories.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleQueryServiceImpl implements RoleQueryService {

    private final RoleRepository roleRepository;

    public RoleQueryServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<Role> handle(GetAllRolesQuery getAllRolesQuery) {
        return roleRepository.findAll();
    }

    @Override
    public Optional<Role> handle(GetRoleByNameQuery getRoleByNameQuery) {
        return roleRepository.findByName(getRoleByNameQuery.name());
    }
}
