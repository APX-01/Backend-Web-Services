package com.education.eduhive.iam.interfaces.rest.resources;

public record AuthenticatedUserResource(
        Long id,
        String email,
        String token
) {
}
