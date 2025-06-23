package com.education.eduhive.iam.interfaces.rest.transform;

import com.education.eduhive.iam.domain.model.commads.CreateUserCommand;
import com.education.eduhive.iam.interfaces.rest.resources.CreateUserResource;

public class CreateUserCommandFromResourceAssembler {
    public static CreateUserCommand toCommandFromResource(CreateUserResource createUserResource) {
        return new CreateUserCommand(
                createUserResource.email(),
                createUserResource.firstName(),
                createUserResource.lastName(),
                createUserResource.password(),
                createUserResource.role());
    }
}
