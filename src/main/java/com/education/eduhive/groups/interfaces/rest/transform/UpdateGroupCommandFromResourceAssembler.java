package com.education.eduhive.groups.interfaces.rest.transform;

import com.education.eduhive.groups.domain.model.commands.UpdateGroupCommand;
import com.education.eduhive.groups.interfaces.rest.resources.UpdateGroupResource;

public class UpdateGroupCommandFromResourceAssembler {
    public static UpdateGroupCommand toCommandFromResource(UpdateGroupResource resource, Long groupId) {
        return new UpdateGroupCommand(
                groupId,
                resource.name(),
                resource.description(),
                resource.imageUrl()
        );
    }
}
