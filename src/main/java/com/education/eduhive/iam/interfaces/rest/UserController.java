package com.education.eduhive.iam.interfaces.rest;

import com.education.eduhive.iam.domain.model.commads.CreateUserCommand;
import com.education.eduhive.iam.domain.model.commads.DeleteUserCommand;
import com.education.eduhive.iam.domain.model.commads.UpdateUserCommand;
import com.education.eduhive.iam.domain.model.queries.GetAllUsersQuery;
import com.education.eduhive.iam.domain.model.queries.GetUserByEmailAndPasswordQuery;
import com.education.eduhive.iam.domain.model.queries.GetUserByEmailQuery;
import com.education.eduhive.iam.domain.model.queries.GetUserByIdQuery;
import com.education.eduhive.iam.domain.services.UserCommandService;
import com.education.eduhive.iam.domain.services.UserQueryService;
import com.education.eduhive.iam.interfaces.rest.resources.CreateUserResource;
import com.education.eduhive.iam.interfaces.rest.resources.UserResource;
import com.education.eduhive.iam.interfaces.rest.resources.UpdateUserResource;
import com.education.eduhive.iam.interfaces.rest.transform.CreateUserCommandFromResourceAssembler;
import com.education.eduhive.iam.interfaces.rest.transform.UserResourceFromEntityAssembler;
import com.education.eduhive.iam.interfaces.rest.transform.UpdateUserCommandFromResourceAssembler;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/api/v1/users", produces = APPLICATION_JSON_VALUE)
@Tag(name = "Users", description = "Operations related to users")
public class UserController {
    private final UserCommandService userCommandService;
    private final UserQueryService userQueryService;

    public UserController(UserCommandService userCommandService, UserQueryService userQueryService) {
        this.userCommandService = userCommandService;
        this.userQueryService = userQueryService;
    }

    @PostMapping
    @Operation(summary = "Create a new user", description = "Creates a new user account.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse (responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<UserResource> createUser(@RequestBody CreateUserResource createUserResource) {
        // Convertir el recurso al comando
        CreateUserCommand createUserCommand = CreateUserCommandFromResourceAssembler.toCommandFromResource(createUserResource);

        // Ejecutar el comando
        var userOptional = userCommandService.handle(createUserCommand);

        // Verificar si el estudiante fue creado exitosamente
        if (userOptional.isPresent()) {
            var userResource = UserResourceFromEntityAssembler.toResourceFromEntity(userOptional.get());
            return ResponseEntity.status(201).body(userResource);
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{userId}")
    @Operation(summary = "Update a user", description = "Update a user by its ID.")
    @ApiResponses(value = {
            @ApiResponse (responseCode = "200", description = "user updated successfully"),
            @ApiResponse (responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<UserResource> updateUser(@PathVariable Long userId, @RequestBody UpdateUserResource updateUserResource){

        //Convertir el recurso a comando
        UpdateUserCommand updateUserCommand = UpdateUserCommandFromResourceAssembler.toCommandFromResource(userId, updateUserResource);

        // Ejecutar el comando
        var userOptional = userCommandService.handle(updateUserCommand);

        // Verificar si el estudiante fue actualizado exitosamente
        if (userOptional.isPresent()) {
            var userResource = UserResourceFromEntityAssembler.toResourceFromEntity(userOptional.get());
            return ResponseEntity.ok(userResource); // 200 OK
        } else {
            return ResponseEntity.badRequest().build();
        }

    }




    @DeleteMapping("/{userId}")
    @Operation(summary = "Delete a user", description = "Deletes a user by its ID.")
    @ApiResponses(value = {
            @ApiResponse (responseCode = "204", description = "User deleted successfully"),
            @ApiResponse (responseCode = "404", description = "User not found")
    })
    public ResponseEntity<UserResource> deleteUser(@PathVariable Long userId) {

        // Crear el comando de eliminación (No se necesita un recurso para eliminar)
        DeleteUserCommand deleteUserCommand = new DeleteUserCommand(userId);

        // Ejecutar el comando de eliminación
        userCommandService.handle(deleteUserCommand);

        // Verificar si el estudiante fue eliminado exitosamente

        return ResponseEntity.noContent().build(); // 204 No Content
    }



    @GetMapping("/{userId}")
    @Operation(summary = "Get a user by ID", description = "Retrieves a user by its ID.")
    @ApiResponses(value = {
            @ApiResponse (responseCode = "200", description = "user retrieved successfully"),
            @ApiResponse (responseCode = "404", description = "user not found")
    })
    public ResponseEntity<UserResource> getUserById(@PathVariable Long userId) {
        //Crear el query para obtener el estudiante por ID
        GetUserByIdQuery getUserByIdQuery = new GetUserByIdQuery(userId);

        // Ejecutar el query
        var userOptional = userQueryService.handle(getUserByIdQuery);

        // Verificar si el estudiante fue encontrado
        if (userOptional.isPresent()) {
            var userResource = UserResourceFromEntityAssembler.toResourceFromEntity(userOptional.get());
            return ResponseEntity.ok(userResource); // 200 OK
        } else {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }

    @GetMapping
    @Operation(summary = "Get all users", description = "Retrieves all users.")
    @ApiResponses(value = {
            @ApiResponse (responseCode = "200", description = "users retrieved successfully"),
            @ApiResponse (responseCode = "404", description = "No users found")
    })
    public ResponseEntity<List<UserResource>> getAllUsers() {

        // Crear el query para obtener todos los estudiantes
        GetAllUsersQuery getAllUsersQuery = new GetAllUsersQuery();

        // Ejecutar el query para obtener todos los estudiantes
        var users = userQueryService.handle(getAllUsersQuery); // null para obtener todos

        // Verificar si se encontraron estudiantes
        if (users.isEmpty()) {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }

        // Convertir la lista de estudiantes a recursos
        var userResources = users.stream()
                .map(UserResourceFromEntityAssembler::toResourceFromEntity)
                .toList();

        return ResponseEntity.ok(userResources); // 200 OK
    }

    @GetMapping("/email/{email}/password/{password}")
    @Operation(summary = "Get a user by email and password ", description = "Retrieves a student by email and password.")
    @ApiResponses( value = {
            @ApiResponse (responseCode = "200", description = "students retrieved successfully"),
            @ApiResponse (responseCode = "404", description = "No students found")
    })
    public ResponseEntity<UserResource> getStudentByEmailAndPassword(@PathVariable String email, @PathVariable String password) {
        // Crear el query para obtener el estudiante por email y password
        GetUserByEmailAndPasswordQuery getUserByEmailAndPasswordQuery = new GetUserByEmailAndPasswordQuery(email, password);

        // Ejecutar el query
        var userOptional = userQueryService.handle(getUserByEmailAndPasswordQuery);

        // Verificar si el estudiante fue encontrado
        if (userOptional.isPresent()) {
            var userResource = UserResourceFromEntityAssembler.toResourceFromEntity(userOptional.get());
            return ResponseEntity.ok(userResource); // 200 OK
        } else {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "Get a user by email", description = "Retrieves a user by email.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User retrieved successfully"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    public ResponseEntity<UserResource> getUserByEmail(@PathVariable String email) {
        //Create the query to get the user by email
        GetUserByEmailQuery getUserByEmailQuery =new GetUserByEmailQuery(email);

        // Execute the query
        var userOptional =userQueryService.handle(getUserByEmailQuery);

        // Check if the user was found
        if (userOptional.isPresent()) {
            var userResource = UserResourceFromEntityAssembler.toResourceFromEntity(userOptional.get());
            return ResponseEntity.ok(userResource); // 200 OK
        } else {
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }
}
