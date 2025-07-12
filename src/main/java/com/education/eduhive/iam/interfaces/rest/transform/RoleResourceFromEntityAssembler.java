package com.education.eduhive.iam.interfaces.rest.transform;

import com.education.eduhive.iam.domain.model.entities.Role;
import com.education.eduhive.iam.interfaces.rest.resources.RoleResource;

public class RoleResourceFromEntityAssembler {
    public static RoleResource toResourceFromEntity(Role roleEntity) {

        return new RoleResource(
                roleEntity.getId(),
                roleEntity.getStringName()
        );
    }
}
