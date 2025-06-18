package com.education.eduhive.groups.interfaces.rest.resources;

public record UpdateGroupResource(
        Long id,
        String name,
        String description,
        String imageUrl
) {
    public UpdateGroupResource {
        if (id == null || id <= 0) {
            throw new IllegalArgumentException("id must be greater than or equal to 0");
        }
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
