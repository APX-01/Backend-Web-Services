package com.education.eduhive.groups.domain.model.commands;

import java.util.Date;

public record SetGroupJoinCodeForGroupCommand(Long groupId, String keycode, Date expiration) {
}
