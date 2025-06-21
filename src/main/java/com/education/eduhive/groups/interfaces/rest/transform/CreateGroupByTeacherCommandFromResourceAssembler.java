package com.education.eduhive.groups.interfaces.rest.transform;

import com.education.eduhive.groups.domain.model.commands.CreateGroupCommand;
import com.education.eduhive.groups.interfaces.rest.resources.CreateGroupByTeacherResource;

public class CreateGroupByTeacherCommandFromResourceAssembler {
    public static CreateGroupCommand toCommandFromResource(CreateGroupByTeacherResource createGroupByTeacherResource, Long teacherId) {
        
        return new CreateGroupCommand(
                createGroupByTeacherResource.name(),
                createGroupByTeacherResource.description(),
                createGroupByTeacherResource.imageUrl(),
                teacherId

        );
    }
}
