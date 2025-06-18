package com.education.eduhive.groups.application.internal.commandservices;

import com.education.eduhive.groups.domain.model.aggregates.Group;
import com.education.eduhive.groups.domain.model.commands.CreateGroupCommand;
import com.education.eduhive.groups.domain.model.commands.DeleteGroupCommand;
import com.education.eduhive.groups.domain.model.commands.UpdateGroupCommand;
import com.education.eduhive.groups.domain.services.GroupCommandService;
import com.education.eduhive.groups.infrastructure.persistence.jpa.repositories.GroupRepository;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class GroupCommandServiceImpl implements GroupCommandService {

    private final GroupRepository groupRepository;

    public GroupCommandServiceImpl(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    @Override
    public Long handle(CreateGroupCommand command) {
        var group = new Group(command);

        try {
            groupRepository.save(group);
            return group.getId();
        } catch (Exception e) {
            throw new RuntimeException("Error while creating group", e);
        }
    }

    @Override
    public Optional<Group> handle(UpdateGroupCommand command) {
        var group = groupRepository.findById(command.id());

        if (group.isEmpty())
        {
            throw new IllegalArgumentException("Group with id " + command.id() + " does not exist");
        }

        var groupToUpdate = group.get();

        try {
            var updatedGroup = groupRepository.save(groupToUpdate.updateGroup(command));
            return Optional.of(updatedGroup);
        } catch (Exception e) {
            throw new RuntimeException("Error while updating group", e);
        }

    }

    @Override
    public void handle(DeleteGroupCommand command) {
        if (!groupRepository.existsById(command.id()))
        {
            throw new IllegalArgumentException("Group with id " + command.id() + " does not exist");
        }
        try {
            groupRepository.deleteById(command.id());
        } catch (Exception e) {
            throw new RuntimeException("Error while deleting group", e);
        }
    }
}
