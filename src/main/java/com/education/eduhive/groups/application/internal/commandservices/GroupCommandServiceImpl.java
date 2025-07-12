package com.education.eduhive.groups.application.internal.commandservices;

import com.education.eduhive.groups.domain.model.aggregates.Group;
import com.education.eduhive.groups.domain.model.commands.*;
import com.education.eduhive.groups.domain.model.valueobjects.GroupJoinCode;
import com.education.eduhive.groups.domain.services.GroupCommandService;
import com.education.eduhive.groups.infrastructure.persistence.jpa.repositories.GroupRepository;
import com.education.eduhive.iam.domain.model.valueobjects.Roles;
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
    public Long handle(CreateGroupCommand command, Long teacherId) {
        // 🔍 Buscar al usuario que creó el grupo
        var teacherOptional = userRepository.findById(teacherId);
        if (teacherOptional.isEmpty()) {
            throw new IllegalArgumentException("Teacher with ID " + teacherId + " not found");
        }

        var teacher = teacherOptional.get();

        var isTeacher = teacher.getRoles().stream()
                .anyMatch(role -> role.getName() == Roles.ROLE_TEACHER);

        // ✅ Validar que tenga rol TEACHER
        if (!isTeacher) {
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
        if (user.getRoles().stream().noneMatch(role -> role.getName().equals(Roles.ROLE_STUDENT))) {
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
    public Optional<GroupJoinCode> handle(SetGroupJoinCodeForGroupCommand setGroupJoinCodeForGroupCommand) {

        // check if the group exists
        var groupOptional = groupRepository.findById(setGroupJoinCodeForGroupCommand.groupId());
        if (groupOptional.isEmpty()) {
            throw new IllegalArgumentException("Group with ID " + setGroupJoinCodeForGroupCommand.groupId() + " does not exist");
        }

        var groupToUpdate = groupOptional.get();

        // Validar que ningún grupo tenga ese key
        if (groupRepository.existsByJoinCode_Key(setGroupJoinCodeForGroupCommand.keycode())) {
            throw new IllegalArgumentException("The key '" + setGroupJoinCodeForGroupCommand.keycode() + "' is already assigned to a group.");
        }

        // Update the group with the new join code
        var joinCodeToAdd = new GroupJoinCode(setGroupJoinCodeForGroupCommand.keycode(), setGroupJoinCodeForGroupCommand.expiration());
        groupToUpdate.setJoinCode(joinCodeToAdd);

        // Save the updated group
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

    @Override
    public void handle(KickStudentFromGroupCommand command, Long teacherId) {

        // 1. Validar que el grupo exista
        var groupOptional = groupRepository.findById(command.groupId());
        if (groupOptional.isEmpty()) {
            throw new IllegalArgumentException("Group with ID " + command.groupId() + " does not exist");
        }
        var group = groupOptional.get();

        // 2. Validar que el usuario a expulsar exista
        var studentOptional = userRepository.findById(command.studentId());
        if (studentOptional.isEmpty()) {
            throw new IllegalArgumentException("User with ID " + command.studentId() + " does not exist");
        }
        var student = studentOptional.get();

        // 3. Validar que el usuario es estudiante
        if (student.getRoles().stream().noneMatch(role -> role.getName().equals(Roles.ROLE_STUDENT))) {
            throw new IllegalArgumentException("User with ID " + command.studentId() + " is not a student");
        }

        // 4. Validar que el profesor logueado esté relacionado como OWNER del grupo
        var teacherOptional = userRepository.findById(teacherId);
        if (teacherOptional.isEmpty()) {
            throw new IllegalArgumentException("Teacher with ID " + teacherId + " does not exist");
        }
        var teacher = teacherOptional.get();

        boolean isTeacherOwnerOfGroup = teacher.getProfilesInGroups().stream()
            .anyMatch(profile -> profile.getGroupId().equals(group.getId()))
            && teacher.getRoles().stream().anyMatch(role -> role.getName().equals(Roles.ROLE_TEACHER));

        if (!isTeacherOwnerOfGroup) {
            throw new IllegalArgumentException("You are not the owner of this group");
        }

        // 5. Validar que el estudiante esté en el grupo
        boolean studentInGroup = student.getProfilesInGroups().stream()
                .anyMatch(profile -> profile.getGroupId().equals(group.getId()));

        if (!studentInGroup) {
            throw new IllegalArgumentException("The student is not a member of this group");
        }

        // 6. Eliminar relación usando método del usuario
        student.removeFromGroup(group.getId());

        // 7. Guardar cambios en repositorio
        userRepository.save(student);
    }




}
