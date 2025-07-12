package com.education.eduhive.groups.interfaces.rest;

import com.education.eduhive.groups.domain.model.commands.DeleteGroupCommand;
import com.education.eduhive.groups.domain.model.commands.JoinGroupByCodeCommand;
import com.education.eduhive.groups.domain.model.commands.KickStudentFromGroupCommand;
import com.education.eduhive.groups.domain.model.queries.GetAllGroupsQuery;
import com.education.eduhive.groups.domain.model.queries.GetGroupByIdQuery;
import com.education.eduhive.groups.domain.model.queries.GetGroupByKeyQuery;
import com.education.eduhive.groups.domain.model.queries.GetGroupsByUserIdQuery;
import com.education.eduhive.groups.domain.services.GroupCommandService;
import com.education.eduhive.groups.domain.services.GroupQueryService;
import com.education.eduhive.groups.interfaces.rest.resources.*;
import com.education.eduhive.groups.interfaces.rest.transform.*;
import com.education.eduhive.iam.infrastructure.authorization.sfs.model.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import org.springframework.security.core.context.SecurityContextHolder;

import java.util.List;

@RestController
@RequestMapping( value = "/api/v1/groups")
@Tag(name = "Groups", description = "Operations related to groups")
public class GroupsController {
    private final GroupCommandService groupCommandService;
    private final GroupQueryService groupQueryService;

    public GroupsController( GroupCommandService groupCommandService, GroupQueryService groupQueryService ) {
        this.groupCommandService = groupCommandService;
        this.groupQueryService = groupQueryService;
    }

    private Long getAuthenticatedUserId() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var principal = auth.getPrincipal();
        if (principal instanceof UserDetailsImpl userDetails) {
            System.out.println("🪪 Authenticated User ID: " + userDetails.getId());
            return userDetails.getId();
        }
        throw new RuntimeException("Invalid principal type");
    }

    @PreAuthorize("hasRole('TEACHER')")
    @PostMapping
    @Operation(summary = "Create a Group", description = "Creates a group with the specified parameters")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "Group Created Successfully"),
                    @ApiResponse(responseCode = "404", description = "Invalid input data")
            }
    )
    public ResponseEntity<GroupResource> createGroup(@RequestBody CreateGroupResource resource) {
        // 1️⃣ Convertir el recurso a comando
        var createCommand = CreateGroupCommandFromResourceAssembler.toCommandFromResource(resource);

        // 2️⃣ Obtener el ID del profesor autenticado
        Long teacherId = getAuthenticatedUserId();

        // 3️⃣ Ejecutar el servicio con el ID del teacher
        var createdId = groupCommandService.handle(createCommand, teacherId);

        // 4️⃣ Validar la creación
        if (createdId == null || createdId <= 0L) {
            return ResponseEntity.badRequest().build();
        }

        // 5️⃣ Recuperar el grupo creado
        var getGroupByIdQuery = new GetGroupByIdQuery(createdId);
        var group = groupQueryService.handle(getGroupByIdQuery,teacherId);

        if (group.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // 6️⃣ Devolver la respuesta
        var groupEntity = group.get();
        var groupResponse = GroupResourceFromEntityAssembler.toResourceFromEntity(groupEntity);
        return new ResponseEntity<>(groupResponse, HttpStatus.CREATED);
    }

//    @PostMapping("/teacher")
//    @Operation(summary = "Create a Group by teacher", description = "Creates a group by a teacher")
//    @ApiResponses(
//            value = {
//                    @ApiResponse(responseCode = "201", description = "Group Created Successfully"),
//                    @ApiResponse(responseCode = "404", description = "Invalid input data")
//            }
//    )
//    public ResponseEntity<GroupResource> createGroupByTeacher(@RequestBody CreateGroupByTeacherResource createGroupByTeacherResource) {
//
//        //Obtain the teacher ID
//        Long teacherId=getTeacherIdFromContext();
//
//        // Create the command from the resource
//        var createGroupCommand = CreateGroupByTeacherCommandFromResourceAssembler
//                .toCommandFromResource(createGroupByTeacherResource);
//
//        // Execute the command using the groupCommandService
//        var groupId = groupCommandService.handle(createGroupCommand);
//
//        if (groupId == null || groupId <= 0L) {
//            return ResponseEntity.badRequest().build();
//        }
//
//        // Retrieve the created group
//        var getGroupByIdQuery = new GetGroupByIdQuery(groupId);
//
//        var group = groupQueryService.handle(getGroupByIdQuery);
//
//        if (group.isEmpty()) {
//            return ResponseEntity.notFound().build();
//        }
//
//        // Convert the group entity to a resource
//        var groupEntity = group.get();
//        var groupResponse = GroupResourceFromEntityAssembler.toResourceFromEntity(groupEntity);
//        return new ResponseEntity<>(groupResponse, HttpStatus.CREATED);
//
//
//    }

    @GetMapping
    @Operation(summary = "Get all groups", description = "Gets all groups")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Groups retrieved Successfully"),
                    @ApiResponse(responseCode = "404", description = "Could not retrieve groups")
            }
    )
    public ResponseEntity<List<GroupResource>> getAllGroups() {
        var getAllGroupsQuery = new GetAllGroupsQuery();
        var groups = groupQueryService.handle(getAllGroupsQuery);

        if (groups.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var groupResponse = groups.stream().map(GroupResourceFromEntityAssembler::toResourceFromEntity).toList();
        return ResponseEntity.ok(groupResponse);
    }

    @GetMapping(value = "/{id}")
    @Operation(summary = "Get group by id", description = "Retrieves a group with the specified id")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Group retrieved successfully"),
                    @ApiResponse(responseCode = "404", description = "Group with specified id does not exist")
            }
    )
    public ResponseEntity<GroupResource> getGroupById(@PathVariable("id") Long id) {
        Long userId = getAuthenticatedUserId();
        var getGroupByIdQuery = new GetGroupByIdQuery(id);
        var group = groupQueryService.handle(getGroupByIdQuery,userId);
        if (group.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var groupEntity = group.get();
        var groupResponse = GroupResourceFromEntityAssembler.toResourceFromEntity(groupEntity);
        return ResponseEntity.ok(groupResponse);
    }

    @PutMapping(value = "/{id}")
    @Operation(summary = "Update a group", description = "Update the group with the specified id")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Group updated successfully"),
                    @ApiResponse(responseCode = "404", description = "Group with specified id does not exist")
            }
    )
    public ResponseEntity<GroupResource> updateGroup(@RequestBody UpdateGroupResource resource, @PathVariable("id") Long id) {

        Long userId = getAuthenticatedUserId();

        var updateCommand = UpdateGroupCommandFromResourceAssembler.toCommandFromResource(resource, id);
        var updatedGroup = groupCommandService.handle(updateCommand);
        if (updatedGroup.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        var getGroupByIdQuery = new GetGroupByIdQuery(id);
        var group = groupQueryService.handle(getGroupByIdQuery,userId);
        if (group.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        var groupEntity = group.get();
        var groupResponse = GroupResourceFromEntityAssembler.toResourceFromEntity(groupEntity);
        return ResponseEntity.ok(groupResponse);
    }

    @DeleteMapping(value = "/{id}")
    @Operation(summary = "Delete group", description = "Delete the group with the specified id")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "204", description = "Group deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Group with the specified id does not exist")
            }
    )
    public ResponseEntity<Void> deleteGroup(@PathVariable("id") Long id) {
        var deleteGroupCommand = new DeleteGroupCommand(id);
        groupCommandService.handle(deleteGroupCommand);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("hasRole('STUDENT')")
    @GetMapping("/join/{key}")
    @Operation(summary = "Join a group via join code")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Joined group successfully"),
            @ApiResponse(responseCode = "404", description = "Group not found")
    })
    public ResponseEntity<GroupResource> joinGroup(
            @PathVariable String key
    ) {

        Long userId = getAuthenticatedUserId();

        var command = new JoinGroupByCodeCommand(userId, key);
        var groupOptional = groupCommandService.handle(command);

        return groupOptional
                .map(group -> ResponseEntity.ok(GroupResourceFromEntityAssembler.toResourceFromEntity(group)))
                .orElseGet(() -> ResponseEntity.badRequest().build());
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "Get groups by user ID", description = "Retrieves all groups that a user belongs to")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Groups retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User not found or no groups found for user")
    })
    public ResponseEntity<List<GroupResource>> getGroupsByUserId(@PathVariable Long userId) {
        // Create the query to get groups by user ID
        var getGroupsByUserIdQuery = new GetGroupsByUserIdQuery(userId);

        // Execute the query using the groupQueryService
        var groups = groupQueryService.handle(getGroupsByUserIdQuery);

        // Check if groups are found
        if (groups.isEmpty()) {
            return ResponseEntity.ok(List.of());
        }

        // Convert the list of groups to a list of GroupResource
        var groupResponse = groups.stream()
                .map(GroupResourceFromEntityAssembler::toResourceFromEntity)
                .toList();
        return ResponseEntity.ok(groupResponse);
    }

    @GetMapping("/groupJoinCode/{key}")
    @Operation(summary = "Get group by join code key")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Group retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "Group not found")
    })
    public ResponseEntity<GroupResource> getGroupByKey(@PathVariable String key) {
        // Create the query to get group by join code key
        var getGroupByKeyQuery = new GetGroupByKeyQuery(key);

        // Execute the query using the groupQueryService
        var groupOptional = groupQueryService.handle(getGroupByKeyQuery);

        return groupOptional
                .map(group -> ResponseEntity.ok(GroupResourceFromEntityAssembler.toResourceFromEntity(group)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{groupId}/students/{studentId}")
    public ResponseEntity<?> kickStudentFromGroup(
            @PathVariable Long groupId,
            @PathVariable Long studentId) {

        Long teacherId = getAuthenticatedUserId(); // 👈 Id del profe logueado desde el JWT

        KickStudentFromGroupCommand command = new KickStudentFromGroupCommand(studentId, groupId);

        groupCommandService.handle(command, teacherId);

        return ResponseEntity.noContent().build(); // 204 No Content ✅
    }
}
