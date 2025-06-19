package com.education.eduhive.iam.interfaces.rest.transform;

import com.education.eduhive.iam.domain.model.aggregates.Student;
import com.education.eduhive.iam.interfaces.rest.resources.ProfileInGroupsResource;
import com.education.eduhive.iam.interfaces.rest.resources.StudentResource;

import java.util.List;
import java.util.stream.Collectors;

public class StudentResourceFromEntityAssembler {
    public static StudentResource toResourceFromEntity(Student student) {
        List<ProfileInGroupsResource> profileResources = student.getProfilesInGroups().stream()
                .map(profile -> new ProfileInGroupsResource(profile.getGroupId(), profile.getScore()))
                .toList();

        return new StudentResource(
                student.getId(),
                student.getEmail(),
                student.getFirstName(),
                student.getLastName(),
                student.getRole(),
                profileResources
        );
    }
}
