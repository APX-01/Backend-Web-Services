package com.education.eduhive.challenges.interfaces.rest;

import com.education.eduhive.challenges.domain.model.aggregates.Challenge;
import com.education.eduhive.challenges.domain.model.commands.CreateChallengeCommand;
import com.education.eduhive.challenges.domain.model.commands.DeleteChallengeCommand;
import com.education.eduhive.challenges.domain.model.queries.GetAllChallengesQuery;
import com.education.eduhive.challenges.domain.model.queries.GetChallengeByIdQuery;
import com.education.eduhive.challenges.domain.model.queries.GetChallengesByGroupIdQuery;
import com.education.eduhive.challenges.domain.services.ChallengeCommandService;
import com.education.eduhive.challenges.domain.services.ChallengeQueryService;
import com.education.eduhive.challenges.interfaces.rest.resource.ChallengeResource;
import com.education.eduhive.challenges.interfaces.rest.resource.CreateChallengeResource;
import com.education.eduhive.challenges.interfaces.rest.resource.UpdateChallengeResource;
import com.education.eduhive.challenges.interfaces.rest.transform.ChallengeResourceFromEntityAssembler;
import com.education.eduhive.challenges.interfaces.rest.transform.CreateChallengeCommandFromResourceAssembler;
import com.education.eduhive.challenges.interfaces.rest.transform.UpdateChallengeCommandFromResourceAssembler;
import com.education.eduhive.iam.infrastructure.authorization.sfs.model.UserDetailsImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/challenges", produces = APPLICATION_JSON_VALUE)
@Tag(name= "Challenges", description = "Operations related to challenges")
public class ChallengesController{
    private final ChallengeCommandService challengeCommandService;
    private final ChallengeQueryService challengeQueryService;


    public ChallengesController(ChallengeCommandService challengeCommandService, ChallengeQueryService challengeQueryService) {
        this.challengeCommandService = challengeCommandService;
        this.challengeQueryService = challengeQueryService;
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
    @Operation(summary = "Create a new challenge", description = "Creates a new challenge with the provided details.")
    @ApiResponses( value = {
        @ApiResponse(responseCode = "201", description = "Challenge created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "404", description = "Challenge not found")
    })
    public ResponseEntity<ChallengeResource> createChallenge(@RequestBody CreateChallengeResource challengeResource){

        Long authenticatedUserId = getAuthenticatedUserId();

        var createdChallenge= CreateChallengeCommandFromResourceAssembler.toCommandFromResource(challengeResource);
        var challengeId=challengeCommandService.handle(createdChallenge, authenticatedUserId);
        if (challengeId==null|| challengeId==0L){
            return ResponseEntity.badRequest().build(); //da una respuestra 400 y vacia
        }
        var getChallengeByIdQuery= new GetChallengeByIdQuery(challengeId);
        var challenge= challengeQueryService.handle(getChallengeByIdQuery,authenticatedUserId);

        if (challenge.isEmpty()){
           return ResponseEntity.notFound().build(); // da una respuesta 404 y vacia
        }
        var challengeEntity=challenge.get();
        var challengeResponse= ChallengeResourceFromEntityAssembler.toResourceFromEntity(challengeEntity);
        return new ResponseEntity<>(challengeResponse, HttpStatus.CREATED ); //201 es creado
    }

    @PutMapping("/{challengeId}")
    @Operation(summary = "Update an existing challenge", description = "Updates the details of an existing challenge.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Challenge updated successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data"),
        @ApiResponse(responseCode = "404", description = "Challenge not found")
    })
    public ResponseEntity<ChallengeResource> updateChallenge(@PathVariable Long challengeId, @RequestBody UpdateChallengeResource updateChallengeResource){
        Long userId = getAuthenticatedUserId();
        var updateChallengeCommand = UpdateChallengeCommandFromResourceAssembler.toCommandFromResource(challengeId,updateChallengeResource);
        var updatedChallenge= challengeCommandService.handle(updateChallengeCommand,userId);
        if (updatedChallenge.isEmpty()){
            return ResponseEntity.notFound().build(); // da una respuesta 404 y vacia
        }

        var updatedChallengeEntity= updatedChallenge.get();
        var challengeResponse= ChallengeResourceFromEntityAssembler.toResourceFromEntity(updatedChallengeEntity);
        return new ResponseEntity<>(challengeResponse,HttpStatus.OK); //200 es ok
    }

    @DeleteMapping("/{challengeId}")
    @Operation(summary = "Delete a challenge", description = "Deletes an existing challenge by its ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Challenge deleted successfully"),
        @ApiResponse(responseCode = "404", description = "Challenge not found")
    })
    public ResponseEntity<ChallengeResource> deleteChallenge(@PathVariable Long challengeId){
        var deleteChallengeCommand = new DeleteChallengeCommand(challengeId);
        challengeCommandService.handle(deleteChallengeCommand);

        return ResponseEntity.noContent().build(); //204 es no content, no hay contenido que devolver
    }

    @GetMapping("/{challengeId}")
    @Operation(summary = "Get a challenge by ID", description = "Retrieves the details of a challenge by its ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Challenge retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "Challenge not found")
    })
    public ResponseEntity<ChallengeResource> getChallengeById(@PathVariable Long challengeId){
        Long authenticatedUserId = getAuthenticatedUserId();

        var getChallengeByIdQuery = new GetChallengeByIdQuery(challengeId);
        var challenge = challengeQueryService.handle(getChallengeByIdQuery,authenticatedUserId);
        if (challenge.isEmpty()){
            return ResponseEntity.notFound().build(); // da una respuesta 404 y vacia
        }
        var challengeEntity= challenge.get();
        var challengeResponse=ChallengeResourceFromEntityAssembler.toResourceFromEntity(challengeEntity);
        return ResponseEntity.ok(challengeResponse); //200 es ok, devuelve el challenge
    }

    @GetMapping
    @Operation(summary = "Get all challenges", description = "Retrieves a list of all challenges.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Challenges retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "No challenges found")
    })
    public ResponseEntity<List<ChallengeResource>> getAllChallenges(){
        var challenges= challengeQueryService.handle(new GetAllChallengesQuery());
        if (challenges.isEmpty()){
            return ResponseEntity.ok(List.of()); // da una respuesta 404 y vacia
        }
        var challengeResources=challenges.stream()
                .map(ChallengeResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(challengeResources); //200 es ok, devuelve la lista de challenges
    }

    @GetMapping("/group/{groupId}")
    @Operation(summary = "Get challenges by group ID", description = "Retrieves a list of challenges associated with a specific group ID.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Challenges retrieved successfully"),
        @ApiResponse(responseCode = "404", description = "No challenges found for the group")
    })
    public ResponseEntity<List<ChallengeResource>> getChallengesByGroupId(@PathVariable Long groupId){

        Long authenticatedUserId = getAuthenticatedUserId();
        var getChallengesByGroupIdQuery=new GetChallengesByGroupIdQuery(groupId);
        var challenges=challengeQueryService.handle(getChallengesByGroupIdQuery,authenticatedUserId);
        if (challenges.isEmpty()){
            return ResponseEntity.notFound().build(); // da una respuesta 404 y vacia
        }
        var challengeResources=challenges.stream()
                .map(ChallengeResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(challengeResources);
    }




}
