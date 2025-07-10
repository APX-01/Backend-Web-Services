package com.education.eduhive.iam.application.internal.commandservices;

import com.education.eduhive.iam.domain.model.commads.SeedRolesCommand;
import com.education.eduhive.iam.domain.model.entities.Role;
import com.education.eduhive.iam.domain.model.valueobjects.Roles;
import com.education.eduhive.iam.domain.services.RoleCommandService;
import com.education.eduhive.iam.infrastructure.persistence.jpa.repositories.RoleRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;

@Service
public class RoleCommandServiceImpl implements RoleCommandService {
    private final RoleRepository roleRepository;

    public RoleCommandServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }


    @Override
    public void handle(SeedRolesCommand seedRolesCommand) {
        Arrays.stream(Roles.values()).forEach(role -> {
            if (!roleRepository.existsByName(role)) {
                roleRepository.save(new Role(Roles.valueOf(role.name())));
            }
        });
    }
}
