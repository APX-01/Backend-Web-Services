package com.education.eduhive.groups.interfaces.rest.transform;

import com.education.eduhive.groups.domain.model.aggregates.Group;
import com.education.eduhive.groups.interfaces.rest.resources.GroupResource;

public class GroupResourceFromEntityAssembler {
    public static GroupResource toResourceFromEntity(Group entity) {
        return new GroupResource(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                entity.getImageUrl()
        );
    }
}
