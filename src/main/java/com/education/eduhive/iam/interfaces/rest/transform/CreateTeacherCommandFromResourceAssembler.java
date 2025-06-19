package com.education.eduhive.iam.interfaces.rest.transform;

import com.education.eduhive.iam.domain.model.commads.CreateTeacherCommand;
import com.education.eduhive.iam.interfaces.rest.resources.CreateTeacherResource;

public class CreateTeacherCommandFromResourceAssembler {
    public static CreateTeacherCommand toCommandFromResource(CreateTeacherResource createTeacherResource){
        return new CreateTeacherCommand(
                createTeacherResource.email(),
                createTeacherResource.firstName(),
                createTeacherResource.lastName(),
                createTeacherResource.password()
        );
    }
}
