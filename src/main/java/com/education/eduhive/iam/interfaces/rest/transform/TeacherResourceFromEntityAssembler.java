package com.education.eduhive.iam.interfaces.rest.transform;

import com.education.eduhive.iam.domain.model.aggregates.Teacher;
import com.education.eduhive.iam.interfaces.rest.resources.ProfileInGroupsResource;
import com.education.eduhive.iam.interfaces.rest.resources.TeacherResource;

import java.util.List;
import java.util.stream.Collectors;

public class TeacherResourceFromEntityAssembler {
    public static TeacherResource toResourceFromEntity(Teacher teacher){

        List<ProfileInGroupsResource> profileInGroupsResources = teacher.getProfilesInGroups().stream()
                .map(profile->new ProfileInGroupsResource(profile.getGroupId(), profile.getScore()))
                .toList();

        return new TeacherResource(
                teacher.getId(),
                teacher.getEmail(),
                teacher.getFirstName(),
                teacher.getLastName(),
                teacher.getRole(),
                profileInGroupsResources
        );
    }
}
