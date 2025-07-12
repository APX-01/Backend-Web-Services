package com.education.eduhive.groups.interfaces.rest.transform;

import com.education.eduhive.groups.domain.model.commands.CreateGroupCommand;
import com.education.eduhive.groups.interfaces.rest.resources.CreateGroupResource;

public class CreateGroupCommandFromResourceAssembler {
    public static CreateGroupCommand toCommandFromResource(CreateGroupResource resource) {
        return new CreateGroupCommand(
                resource.name(),
                resource.description(),
                resource.imageUrl()
        );
    }
}
