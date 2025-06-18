package com.education.eduhive.groups.domain.model.commands;

public record CreateGroupCommand(
        String name,
        String description,
        String imageUrl
) {
    public CreateGroupCommand {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name cannot be null or blank");
        }
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("description cannot be null or blank");
        }
        if (imageUrl == null || imageUrl.isBlank()) {
            throw new IllegalArgumentException("imageUrl cannot be null or blank");
        }
    }
}
