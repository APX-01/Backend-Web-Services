package com.education.eduhive.groups.domain.model.commands;

public record UpdateGroupCommand(
        Long id,
        String name,
        String description,
        String imageUrl
) {
}
