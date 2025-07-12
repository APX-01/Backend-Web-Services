package com.education.eduhive.iam.interfaces.rest.resources;

import com.education.eduhive.iam.domain.model.valueobjects.Roles;

import java.util.List;

public record SignUpResource(
        String email,
        String firstName,
        String lastName,
        String password,
        List<Roles> roles
) {
}
