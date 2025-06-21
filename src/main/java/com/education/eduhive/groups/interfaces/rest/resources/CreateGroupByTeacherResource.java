package com.education.eduhive.groups.interfaces.rest.resources;

public record CreateGroupByTeacherResource(
        String name,
        String description,
        String imageUrl
) {
    public CreateGroupByTeacherResource {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Name cannot be null or blank");
        }
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Description cannot be null or blank");
        }
        if (imageUrl == null || imageUrl.isBlank()) {
            throw new IllegalArgumentException("Image URL cannot be null or blank");
        }
    }
}
