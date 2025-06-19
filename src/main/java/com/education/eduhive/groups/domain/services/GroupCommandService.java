package com.education.eduhive.groups.domain.services;


import com.education.eduhive.groups.domain.model.aggregates.Group;
import com.education.eduhive.groups.domain.model.commands.CreateGroupCommand;
import com.education.eduhive.groups.domain.model.commands.DeleteGroupCommand;
import com.education.eduhive.groups.domain.model.commands.JoinGroupByCodeCommand;
import com.education.eduhive.groups.domain.model.commands.UpdateGroupCommand;

import java.util.Optional;

public interface GroupCommandService {

    Long handle(CreateGroupCommand command);

    Optional<Group> handle(UpdateGroupCommand command);

    void handle(DeleteGroupCommand command);

    Optional<Group> handle(JoinGroupByCodeCommand command);

}
