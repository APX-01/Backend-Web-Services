package com.education.eduhive.iam.interfaces.rest.resources;

import com.education.eduhive.iam.domain.model.valueobjects.Role;

import java.util.List;

public record TeacherResource(
        Long id,
        String email,
        String firstName,
        String lastName,
        Role role,
        List<ProfileInGroupsResource> profilesInGroups
) {
}
