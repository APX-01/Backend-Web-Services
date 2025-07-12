package com.education.eduhive.iam.interfaces.rest.transform;

import com.education.eduhive.iam.domain.model.commads.SignInCommand;
import com.education.eduhive.iam.interfaces.rest.resources.SignInResource;

public class SignInCommandFromResourceAssembler {
    public static SignInCommand toCommandFromResource(SignInResource signInResource){
        return new SignInCommand(
                signInResource.email(),
                signInResource.password()
        );
    }
}
