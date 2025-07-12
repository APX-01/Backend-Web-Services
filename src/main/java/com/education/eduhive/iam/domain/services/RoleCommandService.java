package com.education.eduhive.iam.domain.services;

import com.education.eduhive.iam.domain.model.commads.SeedRolesCommand;

public interface RoleCommandService {

    void handle(SeedRolesCommand seedRolesCommand);

}
