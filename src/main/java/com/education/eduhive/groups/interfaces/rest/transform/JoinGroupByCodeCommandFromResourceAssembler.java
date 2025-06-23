package com.education.eduhive.groups.interfaces.rest.transform;

import com.education.eduhive.groups.domain.model.commands.JoinGroupByCodeCommand;
import com.education.eduhive.groups.interfaces.rest.resources.JoinGroupResource;

public class JoinGroupByCodeCommandFromResourceAssembler {
    public static JoinGroupByCodeCommand toCommandFromResource(JoinGroupResource resource) {
        return new JoinGroupByCodeCommand(resource.userId(), resource.joinKey());
    }
}
