package com.education.eduhive.groups.domain.services;


import com.education.eduhive.groups.domain.model.aggregates.Group;
import com.education.eduhive.groups.domain.model.commands.*;
import com.education.eduhive.groups.domain.model.valueobjects.GroupJoinCode;

import java.util.Optional;

public interface GroupCommandService {

    Long handle(CreateGroupCommand command);

    Optional<Group> handle(UpdateGroupCommand command);

    void handle(DeleteGroupCommand command);

    Optional<Group> handle(JoinGroupByCodeCommand command);

    Optional<GroupJoinCode> handle(SetGroupJoinCodeForGroupCommand command);

    void handle(ResetGroupJoinCodeForGroupCommand command);



}
