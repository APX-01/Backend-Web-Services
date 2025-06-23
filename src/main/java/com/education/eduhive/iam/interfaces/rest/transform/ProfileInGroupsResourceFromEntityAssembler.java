package com.education.eduhive.iam.interfaces.rest.transform;

import com.education.eduhive.iam.domain.model.valueobjects.ProfileInGroup;
import com.education.eduhive.iam.interfaces.rest.resources.ProfileInGroupsResource;

public class ProfileInGroupsResourceFromEntityAssembler {
    public static ProfileInGroupsResource toResourceFromEntity(
            ProfileInGroup profileInGroup
    ) {
        return new ProfileInGroupsResource(profileInGroup.getGroupId(),profileInGroup.getScore());
    }
}
