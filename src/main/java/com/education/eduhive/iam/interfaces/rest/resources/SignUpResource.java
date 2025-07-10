package com.education.eduhive.iam.interfaces.rest.resources;

import com.education.eduhive.iam.domain.model.valueobjects.Roles;

import java.util.List;

public record SignUpResource(
        String email,
        String password,
        List<Roles> roles
) {
}
