package com.education.eduhive.groups.interfaces.rest.transform;

import com.education.eduhive.groups.domain.model.commands.UpdateGroupCommand;
import com.education.eduhive.groups.interfaces.rest.resources.UpdateGroupResource;

public class UpdateGroupCommandFromResourceAssembler {
    public static UpdateGroupCommand toCommandFromResource(UpdateGroupResource resource) {
        return new UpdateGroupCommand(
                resource.id(),
                resource.name(),
                resource.description(),
                resource.imageUrl()
        );
    }
}
