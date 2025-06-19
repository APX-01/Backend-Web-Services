package com.education.eduhive.groups.interfaces.rest;

import com.education.eduhive.groups.domain.model.commands.DeleteGroupCommand;
import com.education.eduhive.groups.domain.model.queries.GetAllGroupsQuery;
import com.education.eduhive.groups.domain.model.queries.GetGroupByIdQuery;
import com.education.eduhive.groups.domain.services.GroupCommandService;
import com.education.eduhive.groups.domain.services.GroupQueryService;
import com.education.eduhive.groups.interfaces.rest.resources.CreateGroupResource;
import com.education.eduhive.groups.interfaces.rest.resources.GroupResource;
import com.education.eduhive.groups.interfaces.rest.resources.JoinGroupResource;
import com.education.eduhive.groups.interfaces.rest.resources.UpdateGroupResource;
import com.education.eduhive.groups.interfaces.rest.transform.CreateGroupCommandFromResourceAssembler;
import com.education.eduhive.groups.interfaces.rest.transform.GroupResourceFromEntityAssembler;
import com.education.eduhive.groups.interfaces.rest.transform.JoinGroupByCodeCommandFromResourceAssembler;
import com.education.eduhive.groups.interfaces.rest.transform.UpdateGroupCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    @Operation(summary = "Create a Group", description = "Creates a group with the specified parameters")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "201", description = "Group Created Successfully"),
                    @ApiResponse(responseCode = "404", description = "Invalid input data")
            }
    )
    public ResponseEntity<GroupResource> createGroup(@RequestBody CreateGroupResource resource) {
        var createCommand = CreateGroupCommandFromResourceAssembler.toCommandFromResource(resource);
        var createdId = groupCommandService.handle(createCommand);

        if (createdId == null || createdId <= 0L) {
            return ResponseEntity.badRequest().build();
        }

        var getGroupByIdQuery = new GetGroupByIdQuery(createdId);
        var group = groupQueryService.handle(getGroupByIdQuery);
        
        if (group.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        
        var groupEntity = group.get();
        var groupResponse = GroupResourceFromEntityAssembler.toResourceFromEntity(groupEntity);
        return new ResponseEntity<>(groupResponse, HttpStatus.CREATED);
    }

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
        var getGroupByIdQuery = new GetGroupByIdQuery(id);
        var group = groupQueryService.handle(getGroupByIdQuery);
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
        var updateCommand = UpdateGroupCommandFromResourceAssembler.toCommandFromResource(resource, id);
        var updatedGroup = groupCommandService.handle(updateCommand);
        if (updatedGroup.isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        var getGroupByIdQuery = new GetGroupByIdQuery(id);
        var group = groupQueryService.handle(getGroupByIdQuery);
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

    @PostMapping("/join")
    @Operation(summary = "Join a group via join code")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Joined group successfully"),
            @ApiResponse(responseCode = "404", description = "Group not found")
    })
    public ResponseEntity<GroupResource> joinGroup(@RequestBody JoinGroupResource joinGroupResource) {
        // Convertir el recurso al comando con el Assembler
        var joinGroupByCodeCommand = JoinGroupByCodeCommandFromResourceAssembler.toCommandFromResource(joinGroupResource);

        // Ejecutar el comando
        var groupOptional = groupCommandService.handle(joinGroupByCodeCommand);

        if (groupOptional.isPresent()) {
            var groupResource = GroupResourceFromEntityAssembler.toResourceFromEntity(groupOptional.get());
            return ResponseEntity.ok(groupResource); // 200 OK
        } else {
            return ResponseEntity.badRequest().build(); // 400 Bad Request
        }
    }
}
