package com.education.eduhive.iam.interfaces.rest.transform;

import com.education.eduhive.iam.domain.model.aggregates.User;
import com.education.eduhive.iam.interfaces.rest.resources.AuthenticatedUserResource;

public class AuthenticatedUserResourceFromEntityAssembler {
    public static AuthenticatedUserResource toResourceFromEntity(User user, String token) {
        return new AuthenticatedUserResource(
                user.getId(),
                user.getEmail(),
                token
        );
    }
}
