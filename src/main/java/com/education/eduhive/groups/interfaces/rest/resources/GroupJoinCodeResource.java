package com.education.eduhive.groups.interfaces.rest.resources;

import java.util.Date;

public record GroupJoinCodeResource(
        String key,
        Date expiration
) {
}
