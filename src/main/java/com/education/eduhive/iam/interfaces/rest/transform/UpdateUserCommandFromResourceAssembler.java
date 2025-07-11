package com.education.eduhive.iam.interfaces.rest.transform;

import com.education.eduhive.iam.domain.model.commads.UpdateUserCommand;
import com.education.eduhive.iam.interfaces.rest.resources.UpdateUserResource;

public class UpdateUserCommandFromResourceAssembler {
    public static UpdateUserCommand toCommandFromResource(UpdateUserResource updateUserResource){
        return new UpdateUserCommand(
                updateUserResource.email(),
                updateUserResource.firstName(),
                updateUserResource.lastName(),
                updateUserResource.password());
    }
}
