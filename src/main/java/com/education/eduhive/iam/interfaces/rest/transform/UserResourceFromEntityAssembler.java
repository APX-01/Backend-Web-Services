package com.education.eduhive.iam.interfaces.rest.transform;

import com.education.eduhive.iam.domain.model.aggregates.User;
import com.education.eduhive.iam.interfaces.rest.resources.ProfileInGroupsResource;
import com.education.eduhive.iam.interfaces.rest.resources.UserResource;

import java.util.List;

public class UserResourceFromEntityAssembler {
    public static UserResource toResourceFromEntity(User user) {
        List<ProfileInGroupsResource> profileResources = user.getProfilesInGroups().stream()
                .map(profile -> new ProfileInGroupsResource(profile.getGroupId(), profile.getScore()))
                .toList();

        return new UserResource(
                user.getId(),
                user.getEmail(),
                user.getFirstName(),
                user.getLastName(),
                user.getRole(),
                profileResources
        );
    }
}
