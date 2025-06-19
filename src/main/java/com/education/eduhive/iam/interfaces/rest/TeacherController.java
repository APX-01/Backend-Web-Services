package com.education.eduhive.iam.interfaces.rest;

import com.education.eduhive.iam.domain.model.commads.*;
import com.education.eduhive.iam.domain.model.queries.GetAllStudentsQuery;
import com.education.eduhive.iam.domain.model.queries.GetAllTeachersQuery;
import com.education.eduhive.iam.domain.model.queries.GetStudentByIdQuery;
import com.education.eduhive.iam.domain.model.queries.GetTeacherByIdQuery;
import com.education.eduhive.iam.domain.services.StudentCommandService;
import com.education.eduhive.iam.domain.services.StudentQueryService;
import com.education.eduhive.iam.domain.services.TeacherCommandService;
import com.education.eduhive.iam.domain.services.TeacherQueryService;
import com.education.eduhive.iam.interfaces.rest.resources.*;
import com.education.eduhive.iam.interfaces.rest.transform.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/teachers", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Teachers", description = "Operations related to teachers")
public class TeacherController {
    private final TeacherCommandService teacherCommandService;
    private final TeacherQueryService teacherQueryService;

    public TeacherController(TeacherCommandService teacherCommandService, TeacherQueryService teacherQueryService) {
        this.teacherCommandService = teacherCommandService;
        this.teacherQueryService = teacherQueryService;
    }

    @PostMapping
    @Operation(summary = "Create a new teacher", description = "Creates a new teacher account.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "teacher created successfully"),
            @ApiResponse (responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<TeacherResource> createTeacher(@RequestBody CreateTeacherResource createTeacherResource) {
        // Convertir el recurso al comando
        CreateTeacherCommand createTeacherCommand= CreateTeacherCommandFromResourceAssembler.toCommandFromResource(createTeacherResource);

        // Ejecutar el comando
        var teacherOptional = teacherCommandService.handle(createTeacherCommand);

        // Verificar si el estudiante fue creado exitosamente
        if (teacherOptional.isPresent()) {
            var resource = TeacherResourceFromEntityAssembler.toResourceFromEntity(teacherOptional.get());
            return ResponseEntity.status(201).body(resource);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{teacherId}")
    @Operation(summary = "Update a teacher", description = "Update a teacher by its ID.")
    @ApiResponses(value = {
            @ApiResponse (responseCode = "200", description = "teacher updated successfully"),
            @ApiResponse (responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<TeacherResource> updateTeacher(@PathVariable Long teacherId, @RequestBody UpdateTeacherResource updateTeacherResource){

        //Convertir el recurso a comando
        UpdateTeacherCommand updateTeacherCommand= UpdateTeacherCommandFromResourceAssembler.toCommandFromResource(teacherId,updateTeacherResource);

        // Ejecutar el comando
        var teacherOptional = teacherCommandService.handle(updateTeacherCommand);

        // Verificar si el estudiante fue actualizado exitosamente
        if (teacherOptional.isPresent()) {
            var resource = TeacherResourceFromEntityAssembler.toResourceFromEntity(teacherOptional.get());
            return ResponseEntity.ok(resource); // 200 OK
        } else {
            return ResponseEntity.badRequest().build();
        }

    }




    @DeleteMapping("/{teacherId}")
    @Operation(summary = "Delete a teacher", description = "Deletes a teacher by its ID.")
    @ApiResponses(value = {
            @ApiResponse (responseCode = "204", description = "teacher deleted successfully"),
            @ApiResponse (responseCode = "404", description = "teacher not found")
    })
    public ResponseEntity<TeacherResource> deleteTeacher(@PathVariable Long teacherId) {

        // Crear el comando de eliminación (No se necesita un recurso para eliminar)
        DeleteTeacherCommand deleteTeacherCommand= new DeleteTeacherCommand(teacherId);

        // Ejecutar el comando de eliminación
        teacherCommandService.handle(deleteTeacherCommand);

        // Verificar si el estudiante fue eliminado exitosamente

        return ResponseEntity.noContent().build(); // 204 No Content
    }



    @GetMapping("/{teacherId}")
    @Operation(summary = "Get a teacher by ID", description = "Retrieves a teacher by its ID.")
    @ApiResponses(value = {
            @ApiResponse (responseCode = "200", description = "teacher retrieved successfully"),
            @ApiResponse (responseCode = "404", description = "teacher not found")
    })
    public ResponseEntity<TeacherResource> getTeacherById(@PathVariable Long teacherId) {
        //Crear el query para obtener el estudiante por ID
        GetTeacherByIdQuery getTeacherByIdQuery = new GetTeacherByIdQuery(teacherId);

        // Ejecutar el query
        var teacherOptional = teacherQueryService.handle(getTeacherByIdQuery);

        // Verificar si el estudiante fue encontrado
        if (teacherOptional.isPresent()) {
            var teacherResource = TeacherResourceFromEntityAssembler.toResourceFromEntity(teacherOptional.get());
            return ResponseEntity.ok(teacherResource); // 200 OK
        } else {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }

    @GetMapping
    @Operation(summary = "Get all teacher", description = "Retrieves all teacher.")
    @ApiResponses(value = {
            @ApiResponse (responseCode = "200", description = "teacher retrieved successfully"),
            @ApiResponse (responseCode = "404", description = "No teacher found")
    })
    public ResponseEntity<List<TeacherResource>> getAllTeachers() {

        // Crear el query para obtener todos los teachers
        GetAllTeachersQuery getAllTeachersQuery = new GetAllTeachersQuery();

        // Ejecutar el query para obtener todos los teachers
        var teachers = teacherQueryService.handle(getAllTeachersQuery); // null para obtener todos

        // Verificar si se encontraron teachers
        if (teachers.isEmpty()) {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }

        // Convertir la lista de estudiantes a recursos
        var teacherResources = teachers.stream()
                .map(TeacherResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(teacherResources); // 200 OK
    }
}