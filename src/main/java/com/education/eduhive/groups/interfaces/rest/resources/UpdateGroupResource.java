package com.education.eduhive.groups.interfaces.rest.resources;

public record UpdateGroupResource(
        String name,
        String description,
        String imageUrl
) {
    public UpdateGroupResource {
        if (name == null || name.isEmpty()) {
            throw new IllegalArgumentException("name cannot be null or empty");
        }
        if (description == null || description.isEmpty()) {
            throw new IllegalArgumentException("description cannot be null or empty");
        }
        if (imageUrl == null || imageUrl.isEmpty()) {
            throw new IllegalArgumentException("imageUrl cannot be null or empty");
        }
    }
}
