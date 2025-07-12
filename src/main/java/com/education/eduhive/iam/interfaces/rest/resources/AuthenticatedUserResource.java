package com.education.eduhive.iam.interfaces.rest.resources;

import java.util.List;

public record AuthenticatedUserResource(
        Long id,
        String email,
        String token,
        List<String> roles
) {
}
