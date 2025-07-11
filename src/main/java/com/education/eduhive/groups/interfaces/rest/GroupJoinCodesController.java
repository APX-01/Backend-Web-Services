package com.education.eduhive.groups.interfaces.rest;

import com.education.eduhive.groups.domain.model.commands.DeleteGroupCommand;
import com.education.eduhive.groups.domain.model.commands.ResetGroupJoinCodeForGroupCommand;
import com.education.eduhive.groups.domain.model.commands.SetGroupJoinCodeForGroupCommand;
import com.education.eduhive.groups.domain.model.queries.GetAllGroupsQuery;
import com.education.eduhive.groups.domain.model.queries.GetGroupByIdQuery;
import com.education.eduhive.groups.domain.model.valueobjects.GroupJoinCode;
import com.education.eduhive.groups.domain.services.GroupCommandService;
import com.education.eduhive.groups.domain.services.GroupQueryService;
import com.education.eduhive.groups.interfaces.rest.resources.*;
import com.education.eduhive.groups.interfaces.rest.transform.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping( value = "/api/v1/groups/{groupId}/groupJoinCodes")
@Tag(name = "Groups")
public class GroupJoinCodesController {
    private final GroupCommandService groupCommandService;
    private final GroupQueryService groupQueryService;

    public GroupJoinCodesController(GroupCommandService groupCommandService, GroupQueryService groupQueryService ) {
        this.groupCommandService = groupCommandService;
        this.groupQueryService = groupQueryService;
    }

    @GetMapping
    @Operation(summary = "Get group join code", description = "Get a group join code with a specified group id")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Join code successfully retrieved"),
                    @ApiResponse(responseCode = "404", description = "Group not found or group doesn't have a join code")
            }
    )
    public ResponseEntity<GroupJoinCode> getGroupJoinCodeByGroupId(@PathVariable Long groupId)
    {
        var getGroupByIdQuery = new GetGroupByIdQuery(groupId);
        var group = this.groupQueryService.handle(getGroupByIdQuery);

        if (group.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var joinCode = group.get().getJoinCode();

        if (joinCode == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(joinCode);
    }

    @PreAuthorize("hasRole('TEACHER')")
    @PutMapping
    @Operation(summary = "Set group join code", description = "Set a group join code with for a specified group id")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Join code successfully set"),
                    @ApiResponse(responseCode = "404", description = "Group not found or group already has a join code")
            }
    )
    public ResponseEntity<GroupJoinCodeResource> setGroupJoinCodeByGroupId(@PathVariable Long groupId, @RequestBody SetGroupJoinCodeResource resource) {
        var getGroupByIdQuery = new GetGroupByIdQuery(groupId);
        var group = this.groupQueryService.handle(getGroupByIdQuery);

        if (group.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var groupToUpdate = group.get();

        if (groupToUpdate.getJoinCode() != null) {
            return ResponseEntity.badRequest().build();
        }

        var setGroupJoinCodeCommand = SetGroupJoinCodeCommandFromResourceAssembler.toCommandFromResource(groupId, resource);

        var joinCode = this.groupCommandService.handle(setGroupJoinCodeCommand);

        if (joinCode.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var joinCodeResponse = GroupJoinCodeResourceFromEntityAssembler.toResourceFromEntity(joinCode.get());

        return ResponseEntity.ok(joinCodeResponse);
    }

    @PutMapping("/reset")
    @Operation(summary = "Reset group join code", description = "Reset a group join code with for a specified group id")
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "Join code successfully reset"),
                    @ApiResponse(responseCode = "404", description = "Group not found or group does not has a join code")
            }
    )
    public ResponseEntity<Void> resetGroupJoinCodeByGroupId(@PathVariable Long groupId) {
        var getGroupByIdQuery = new GetGroupByIdQuery(groupId);
        var group = this.groupQueryService.handle(getGroupByIdQuery);

        if (group.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        var groupToUpdate = group.get();

        if (groupToUpdate.getJoinCode() == null) {
            return ResponseEntity.badRequest().build();
        }

        this.groupCommandService.handle(new ResetGroupJoinCodeForGroupCommand(groupId));

        return ResponseEntity.ok().build();
    }
}
