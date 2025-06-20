package com.education.eduhive.groups.interfaces.rest.transform;

import com.education.eduhive.groups.domain.model.commands.SetGroupJoinCodeForGroupCommand;
import com.education.eduhive.groups.interfaces.rest.resources.SetGroupJoinCodeResource;

public class SetGroupJoinCodeCommandFromResourceAssembler {
    public static SetGroupJoinCodeForGroupCommand toCommandFromResource(Long groupId, SetGroupJoinCodeResource command) {
        return new SetGroupJoinCodeForGroupCommand(
                groupId,
                command.key(),
                command.expiration()
        );
    }
}
