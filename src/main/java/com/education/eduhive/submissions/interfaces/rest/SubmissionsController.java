package com.education.eduhive.submissions.interfaces.rest;

import com.education.eduhive.submissions.domain.model.aggregates.Submission;
import com.education.eduhive.submissions.domain.model.commands.DeleteSubmissionCommand;
import com.education.eduhive.submissions.domain.model.queries.*;
import com.education.eduhive.submissions.domain.services.SubmissionCommandService;
import com.education.eduhive.submissions.domain.services.SubmissionQueryService;
import com.education.eduhive.submissions.interfaces.rest.resources.CreateSubmissionResource;
import com.education.eduhive.submissions.interfaces.rest.resources.SubmissionResource;
import com.education.eduhive.submissions.interfaces.rest.resources.UpdateSubmissionResource;
import com.education.eduhive.submissions.interfaces.rest.transform.CreateSubmissionCommandFromResourceAssembler;
import com.education.eduhive.submissions.interfaces.rest.transform.SubmissionResourceFromEntityAssembler;
import com.education.eduhive.submissions.interfaces.rest.transform.UpdateSubmissionCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/submissions", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Submissions", description = "Operations related to submissions")
public class SubmissionsController {
    private final SubmissionCommandService submissionCommandService;
    private final SubmissionQueryService submissionQueryService;

    public SubmissionsController(SubmissionCommandService submissionCommandService, SubmissionQueryService submissionQueryService) {
        this.submissionCommandService = submissionCommandService;
        this.submissionQueryService = submissionQueryService;
    }

    @PostMapping
    @Operation(summary = "Create a new submission", description = "Creates a new submission for a challenge by a student.")
    @ApiResponses(value = {
            @ApiResponse (responseCode = "201", description = "Submission created successfully"),
            @ApiResponse (responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<SubmissionResource> createSubmission(@RequestBody CreateSubmissionResource submissionResource){

        var createdSubmission= CreateSubmissionCommandFromResourceAssembler.toCommandFromResource(submissionResource);
        var submissionId = submissionCommandService.handle(createdSubmission);
        if(submissionId ==null|| submissionId ==0L) {
            return ResponseEntity.badRequest().build();//da una respuestra 400 y vacia
        }
        var getSubmissionByIdQuery = new GetSubmissionByIdQuery(submissionId);
        var submission= submissionQueryService.handle(getSubmissionByIdQuery);

        if(submission.isEmpty()) {
            return ResponseEntity.notFound().build(); //404 Not Found
        }
        var submissionEntity= submission.get();
        var submissionResponse= SubmissionResourceFromEntityAssembler.toResourceFromEntity(submissionEntity);
        return new ResponseEntity<>(submissionResponse, HttpStatus.CREATED);
    }

    @PutMapping("/{submissionId}")
    @Operation(summary = "Update a submission", description = "Update a submission's score by its ID.")
    @ApiResponses(value = {
            @ApiResponse (responseCode = "200", description = "Submission updated successfully"),
            @ApiResponse (responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<SubmissionResource> updateSubmission(@PathVariable Long submissionId, @RequestBody UpdateSubmissionResource updateSubmissionResource){
        var updateSubmissionCommand= UpdateSubmissionCommandFromResourceAssembler.toCommandFromResource(submissionId, updateSubmissionResource);
        var updatedSubmission= submissionCommandService.handle(updateSubmissionCommand);
        if (updatedSubmission.isEmpty()) {
            return ResponseEntity.badRequest().build(); //400 Bad Request
        }

        var updatedSubmissionEntity= updatedSubmission.get();
        var updatedSubmissionResponse= SubmissionResourceFromEntityAssembler.toResourceFromEntity(updatedSubmissionEntity);
        return ResponseEntity.ok(updatedSubmissionResponse);//200
    }

    @DeleteMapping("/{submissionId}")
    @Operation(summary = "Delete a submission", description = "Deletes a submission by its ID.")
    @ApiResponses(value = {
            @ApiResponse (responseCode = "204", description = "Submission deleted successfully"),
            @ApiResponse (responseCode = "404", description = "Submission not found")
    })
    public ResponseEntity<SubmissionResource> deleteSubmission(@PathVariable Long submissionId){

        var deleteSubmissionCommand = new DeleteSubmissionCommand(submissionId);
        submissionCommandService.handle(deleteSubmissionCommand);

        return ResponseEntity.noContent().build(); //204 No Content
    }

    @GetMapping("/{submissionId}")
    @Operation(summary = "Get a submission by ID", description = "Retrieves a submission by its ID.")
    @ApiResponses(value = {
            @ApiResponse (responseCode = "200", description = "Submission retrieved successfully"),
            @ApiResponse (responseCode = "404", description = "Submission not found")
    })
    public ResponseEntity<SubmissionResource> getSubmissionById(@PathVariable Long submissionId){
        var getSubmissionByIdQuery= new GetSubmissionByIdQuery(submissionId);
        var submission = submissionQueryService.handle(getSubmissionByIdQuery);
        if (submission.isEmpty()) {
            return ResponseEntity.notFound().build(); //404 Not Found
        }

        var submissionEntity = submission.get();
        var submissionResponse= SubmissionResourceFromEntityAssembler.toResourceFromEntity(submissionEntity);
        return ResponseEntity.ok(submissionResponse); //200 OK

    }

    @GetMapping
    @Operation(summary = "Get all submissions", description = "Retrieves all submissions.")
    @ApiResponses(value = {
            @ApiResponse (responseCode = "200", description = "Submissions retrieved successfully"),
            @ApiResponse (responseCode = "404", description = "No submissions found")
    })
    public ResponseEntity<List<SubmissionResource>> getAllSubmissions(){
        var submissions = submissionQueryService.handle(new GetAllSubmissionsQuery());
        if (submissions.isEmpty()) {
            return ResponseEntity.notFound().build(); //404 Not Found
        }

        var submissionResources = submissions.stream()
                .map(SubmissionResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(submissionResources);
    }

    @GetMapping("/challenges/{challengeId}/submissions")
    @Operation(summary = "Get submissions by challengeId", description = "Retrieves submissions by challengeId.")
    @ApiResponses(value = {
            @ApiResponse (responseCode = "200", description = "Submissions retrieved successfully"),
            @ApiResponse (responseCode = "404", description = "No submissions found")
    })
    public ResponseEntity<List<SubmissionResource>> getSubmissionsByChallengeId(@PathVariable Long challengeId) {
        var getSubmissionsByChallengeIdQuery = new GetSubmissionsByChallengeIdQuery(challengeId);
        var submissions = submissionQueryService.handle(getSubmissionsByChallengeIdQuery);
        if (submissions.isEmpty()) {
            return ResponseEntity.notFound().build(); //404 Not Found
        }

        var submissionResources = submissions.stream()
                .map(SubmissionResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(submissionResources);
    }

    @GetMapping("/students/{studentId}/submissions")
    @Operation(summary = "Get submissions by studentId", description = "Retrieves submissions submitted by a specific student.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Submissions retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "No submissions found for the given student")
    })
    public ResponseEntity<List<SubmissionResource>> getSubmissionsByStudentId(@PathVariable Long studentId) {
        //Create the query to get submissions by studentId
        var getSubmissionsByStudentIdQuery = new GetSubmissionsByStudentIdQuery(studentId);

        // Execute the query
        var submissionsOptional = submissionQueryService.handle(getSubmissionsByStudentIdQuery);

        //Verify if submissions were found
        if (submissionsOptional.isEmpty()) {
            return ResponseEntity.notFound().build(); // 404
        }
        //cada entidad de dominio (Submission) en un DTO o recurso (SubmissionResource) que es más adecuado para enviar como respuesta HTTP
        // Convert every Submission entity to SubmissionResource (resource o DTO) to send as HTTP response
        var resources = submissionsOptional.stream()
                .map(SubmissionResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(resources); // 200
    }

    @GetMapping("/students/{studentId}/challenges/{challengeId}")
    @Operation(summary = "Get submissions by studentId and challengeId", description = "Retrieves submissions submitted by a specific student for a specific challenge.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Submissions retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "No submissions found for the given student and challenge")
    })
    public ResponseEntity<List<SubmissionResource>> getSubmissionsByStudentIdAndChallengeId(@PathVariable Long studentId, @PathVariable Long challengeId) {
        // Create the query
        var getSubmissionsByStudentIdAndChallengeIdQuery = new GetSubmissionsByStudentIdAndChallengeIdQuery(studentId, challengeId);

        // Execute the query
        var submissionsOptional = submissionQueryService.handle(getSubmissionsByStudentIdAndChallengeIdQuery);

        // Verify if submissions were found
        if (submissionsOptional.isEmpty()) {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }

        // Convert every Submission entity to SubmissionResource (resource o DTO) to send as HTTP response
        var submissionResources = submissionsOptional.stream()
                .map(SubmissionResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(submissionResources); // 200 OK
    }



}
