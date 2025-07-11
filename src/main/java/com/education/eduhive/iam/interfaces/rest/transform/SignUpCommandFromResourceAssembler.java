package com.education.eduhive.iam.interfaces.rest.transform;

import com.education.eduhive.iam.domain.model.commads.SignUpCommand;
import com.education.eduhive.iam.interfaces.rest.resources.SignUpResource;

public class SignUpCommandFromResourceAssembler {
    public static SignUpCommand toCommandFromResource(SignUpResource signUpResource) {
        return new SignUpCommand(
                signUpResource.email(),
                signUpResource.firstName(),
                signUpResource.lastName(),
                signUpResource.password(),
                signUpResource.roles()
        );
    }
}
