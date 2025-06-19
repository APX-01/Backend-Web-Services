package com.education.eduhive.iam.interfaces.rest;

import com.education.eduhive.iam.domain.model.aggregates.Student;
import com.education.eduhive.iam.domain.model.commads.CreateStudentCommand;
import com.education.eduhive.iam.domain.model.commads.DeleteStudentCommand;
import com.education.eduhive.iam.domain.model.commads.UpdateStudentCommand;
import com.education.eduhive.iam.domain.model.queries.GetAllStudentsQuery;
import com.education.eduhive.iam.domain.model.queries.GetStudentByIdQuery;
import com.education.eduhive.iam.domain.services.StudentCommandService;
import com.education.eduhive.iam.domain.services.StudentQueryService;
import com.education.eduhive.iam.interfaces.rest.resources.CreateStudentResource;
import com.education.eduhive.iam.interfaces.rest.resources.StudentResource;
import com.education.eduhive.iam.interfaces.rest.resources.UpdateStudentResource;
import com.education.eduhive.iam.interfaces.rest.transform.CreateStudentCommandFromResourceAssembler;
import com.education.eduhive.iam.interfaces.rest.transform.StudentResourceFromEntityAssembler;
import com.education.eduhive.iam.interfaces.rest.transform.UpdateStudentCommandFromResourceAssembler;
import com.education.eduhive.submissions.interfaces.rest.resources.SubmissionResource;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/students", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Students", description = "Operations related to students")
public class StudentController {
    private final StudentCommandService studentCommandService;
    private final StudentQueryService studentQueryService;

    public StudentController(StudentCommandService studentCommandService, StudentQueryService studentQueryService) {
        this.studentCommandService = studentCommandService;
        this.studentQueryService = studentQueryService;
    }

    @PostMapping
    @Operation(summary = "Create a new student", description = "Creates a new student account.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Student created successfully"),
            @ApiResponse (responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<StudentResource> createStudent(@RequestBody CreateStudentResource createStudentResource) {
        // Convertir el recurso al comando
        CreateStudentCommand createStudentCommand= CreateStudentCommandFromResourceAssembler.toCommandFromResource(createStudentResource);

        // Ejecutar el comando
        var studentOptional = studentCommandService.handle(createStudentCommand);

        // Verificar si el estudiante fue creado exitosamente
        if (studentOptional.isPresent()) {
            var resource = StudentResourceFromEntityAssembler.toResourceFromEntity(studentOptional.get());
            return ResponseEntity.status(201).body(resource);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{studentId}")
    @Operation(summary = "Update a student", description = "Update a student by its ID.")
    @ApiResponses(value = {
            @ApiResponse (responseCode = "200", description = "Student updated successfully"),
            @ApiResponse (responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<StudentResource> updateStudent(@PathVariable Long studentId, @RequestBody UpdateStudentResource updateStudentResource){

        //Convertir el recurso a comando
        UpdateStudentCommand updateStudentCommand= UpdateStudentCommandFromResourceAssembler.toCommandFromResource(studentId,updateStudentResource);

        // Ejecutar el comando
        var studentOptional = studentCommandService.handle(updateStudentCommand);

        // Verificar si el estudiante fue actualizado exitosamente
        if (studentOptional.isPresent()) {
            var resource = StudentResourceFromEntityAssembler.toResourceFromEntity(studentOptional.get());
            return ResponseEntity.ok(resource); // 200 OK
        } else {
            return ResponseEntity.badRequest().build();
        }

    }




    @DeleteMapping("/{studentId}")
    @Operation(summary = "Delete a student", description = "Deletes a student by its ID.")
    @ApiResponses(value = {
            @ApiResponse (responseCode = "204", description = "Student deleted successfully"),
            @ApiResponse (responseCode = "404", description = "Student not found")
    })
    public ResponseEntity<StudentResource> deleteStudent(@PathVariable Long studentId) {

        // Crear el comando de eliminación (No se necesita un recurso para eliminar)
        DeleteStudentCommand deleteStudentCommand= new DeleteStudentCommand(studentId);

        // Ejecutar el comando de eliminación
        studentCommandService.handle(deleteStudentCommand);

        // Verificar si el estudiante fue eliminado exitosamente

        return ResponseEntity.noContent().build(); // 204 No Content
    }



    @GetMapping("/{studentId}")
    @Operation(summary = "Get a student by ID", description = "Retrieves a student by its ID.")
    @ApiResponses(value = {
            @ApiResponse (responseCode = "200", description = "student retrieved successfully"),
            @ApiResponse (responseCode = "404", description = "student not found")
    })
    public ResponseEntity<StudentResource> getStudentById(@PathVariable Long studentId) {
        //Crear el query para obtener el estudiante por ID
        GetStudentByIdQuery getStudentByIdQuery = new GetStudentByIdQuery(studentId);

        // Ejecutar el query
        var studentOptional = studentQueryService.handle(getStudentByIdQuery);

        // Verificar si el estudiante fue encontrado
        if (studentOptional.isPresent()) {
            var studentResource = StudentResourceFromEntityAssembler.toResourceFromEntity(studentOptional.get());
            return ResponseEntity.ok(studentResource); // 200 OK
        } else {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }

    @GetMapping
    @Operation(summary = "Get all students", description = "Retrieves all students.")
    @ApiResponses(value = {
            @ApiResponse (responseCode = "200", description = "students retrieved successfully"),
            @ApiResponse (responseCode = "404", description = "No students found")
    })
    public ResponseEntity<List<StudentResource>> getAllStudents() {

        // Crear el query para obtener todos los estudiantes
        GetAllStudentsQuery getAllStudentsQuery = new GetAllStudentsQuery();

        // Ejecutar el query para obtener todos los estudiantes
        var students = studentQueryService.handle(getAllStudentsQuery); // null para obtener todos

        // Verificar si se encontraron estudiantes
        if (students.isEmpty()) {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }

        // Convertir la lista de estudiantes a recursos
        var studentResources = students.stream()
                .map(StudentResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(studentResources); // 200 OK
    }
}
