package com.education.eduhive.groups.interfaces.rest.transform;

import com.education.eduhive.groups.domain.model.valueobjects.GroupJoinCode;
import com.education.eduhive.groups.interfaces.rest.resources.GroupJoinCodeResource;

public class GroupJoinCodeResourceFromEntityAssembler {
    public static GroupJoinCodeResource toResourceFromEntity(GroupJoinCode entity) {
        return new GroupJoinCodeResource(
                entity.key(),
                entity.expiration()
        );
    }
}
