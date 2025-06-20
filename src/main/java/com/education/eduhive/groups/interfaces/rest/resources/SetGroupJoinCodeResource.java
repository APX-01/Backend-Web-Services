package com.education.eduhive.groups.interfaces.rest.resources;

import java.util.Date;

public record SetGroupJoinCodeResource(
        String key,
        Date expiration
) {
}
