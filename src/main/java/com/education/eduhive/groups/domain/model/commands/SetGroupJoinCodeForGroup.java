package com.education.eduhive.groups.domain.model.commands;

import java.util.Date;

public record SetGroupJoinCodeForGroup(Long groupId, String keycode, Date expiration) {
}
