package com.education.eduhive.groups.application.internal.commandservices;

import com.education.eduhive.groups.domain.model.aggregates.Group;
import com.education.eduhive.groups.domain.model.commands.*;
import com.education.eduhive.groups.domain.model.valueobjects.GroupJoinCode;
import com.education.eduhive.groups.domain.services.GroupCommandService;
import com.education.eduhive.groups.infrastructure.persistence.jpa.repositories.GroupRepository;
import com.education.eduhive.iam.domain.model.valueobjects.Role;
import com.education.eduhive.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class GroupCommandServiceImpl implements GroupCommandService {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    public GroupCommandServiceImpl(GroupRepository groupRepository, UserRepository userRepository) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Long handle(CreateGroupCommand command) {
        // 🔍 Buscar al usuario que creó el grupo
        var teacherOptional = userRepository.findById(command.teacherId());
        if (teacherOptional.isEmpty()) {
            throw new IllegalArgumentException("Teacher with ID " + command.teacherId() + " not found");
        }

        var teacher = teacherOptional.get();

        // ✅ Validar que tenga rol TEACHER
        if (!teacher.getRole().equals(Role.ROLE_TEACHER)) {
            throw new IllegalArgumentException("Only teachers can create groups");
        }

        // ✅ Crear y guardar el grupo
        var group = new Group(command);
        groupRepository.save(group);

        // ➕ Asignar grupo al teacher
        teacher.assignToGroup(group.getId());
        userRepository.save(teacher);

        return group.getId();
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
            // 1. Buscar todos los usuarios
            var allUsers = userRepository.findAll();

            // 2. Remover el grupo de cada usuario usando tu método
            allUsers.forEach(user -> {
                user.removeFromGroup(command.id());
                userRepository.save(user); // se guarda aunque no haya sido modificado, por simplicidad
            });

            groupRepository.deleteById(command.id());
        } catch (Exception e) {
            throw new RuntimeException("Error while deleting group", e);
        }
    }

    //NEW METHOD
    @Override
    public Optional<Group> handle(JoinGroupByCodeCommand command) {
        // Buscar grupo con ese key
        var groupOptional = groupRepository.findAll().stream()
                .filter(group -> group.getJoinCode() != null &&
                        group.getJoinCode().key().equals(command.joinKey()))
                .findFirst();

        if (groupOptional.isEmpty()) {
            throw new IllegalArgumentException("Invalid join code");
        }

        var group = groupOptional.get();

        // Validar expiración
        if (group.getJoinCode().expiration().before(new Date())) {
            throw new IllegalStateException("Join code has expired");
        }

        // Buscar usuario
        var userOptional = userRepository.findById(command.userId());
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User not found");
        }

        var user = userOptional.get();

        // Solo permitir que se unan los estudiantes
        if (!user.getRole().equals(Role.ROLE_STUDENT)) {
            throw new IllegalStateException("Only students can join groups via code");
        }

        // Verificar si ya está unido
        boolean alreadyInGroup = user.getProfilesInGroups().stream()
                .anyMatch(p -> p.getGroupId().equals(group.getId()));

        if (!alreadyInGroup) {
            user.assignToGroup(group.getId());
            userRepository.save(user);
        }

        return Optional.of(group);
    }

    @Override
    public Optional<GroupJoinCode> handle(SetGroupJoinCodeForGroupCommand command) {
        var group = groupRepository.findById(command.groupId());

        if (group.isEmpty())
        {
            throw new IllegalArgumentException("Group with id " + command.groupId() + " does not exist");
        }

        var groupToUpdate = group.get();
        var joinCodeToAdd = new GroupJoinCode(command.keycode(), command.expiration());
        groupToUpdate.setJoinCode(joinCodeToAdd);

        try {
            groupRepository.save(groupToUpdate);
            return Optional.of(groupToUpdate.getJoinCode());
        } catch (Exception e) {
            throw new RuntimeException("Error while updating join code", e);
        }
    }

    @Override
    public void handle(ResetGroupJoinCodeForGroupCommand command) {
        var group = groupRepository.findById(command.groupId());

        if (group.isEmpty()) {
            throw new IllegalArgumentException("Group with id " + command.groupId() + " does not exist");
        }

        var groupToUpdate = group.get();
        groupToUpdate.resetJoinCode();

        try {
            groupRepository.save(groupToUpdate);
        } catch (Exception e) {
            throw new RuntimeException("Error while removing join code", e);
        }
    }
}
