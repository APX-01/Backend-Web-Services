package com.education.eduhive.groups.interfaces.rest.resources;

public record GroupResource(
        Long id,
        String name,
        String description,
        String imageUrl
) {
}
