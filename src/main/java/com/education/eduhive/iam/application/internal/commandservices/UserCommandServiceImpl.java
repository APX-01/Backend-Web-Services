package com.education.eduhive.iam.application.internal.commandservices;

import com.education.eduhive.iam.application.internal.outboundservices.hashing.HashingService;
import com.education.eduhive.iam.application.internal.outboundservices.tokens.TokenService;
import com.education.eduhive.iam.domain.model.aggregates.User;
import com.education.eduhive.iam.domain.model.commads.*;
import com.education.eduhive.iam.domain.services.UserCommandService;
import com.education.eduhive.iam.infrastructure.persistence.jpa.repositories.RoleRepository;
import com.education.eduhive.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserCommandServiceImpl implements UserCommandService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final HashingService hashingService;
    private final TokenService tokenService;

    public UserCommandServiceImpl(UserRepository userRepository, RoleRepository roleRepository, HashingService hashingService, TokenService tokenService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.hashingService = hashingService;
        this.tokenService = tokenService;
    }

//    @Override
//    public Optional<User> handle(CreateUserCommand createUserCommand) {
//
//        //Check if a user with the same email already exists
//        if (userRepository.existsByEmail(createUserCommand.email())) {
//            throw new IllegalArgumentException("User with email " + createUserCommand.email() + " already exists");
//        }
//
//        var user = new User(createUserCommand);
//
//        try{
//            userRepository.save(user);
//            return Optional.of(user);
//        } catch (Exception e) {
//            // Handle exception, e.g., log it or rethrow as a custom exception
//            return Optional.empty();
//        }
//    }

    @Override
    public Optional<User> handle(UpdateUserCommand updateUserCommand) {
        var userOptional = userRepository.findById(updateUserCommand.userId());
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User with ID " + updateUserCommand.userId() + " not found");
        }

        //Check if a user with the same email already exists
        var existingUserWithEmail = userRepository.findByEmail(updateUserCommand.email());
        if (existingUserWithEmail.isPresent() && !existingUserWithEmail.get().getId().equals(updateUserCommand.userId())) {
            throw new IllegalArgumentException("User with email " + updateUserCommand.email() + " already exists");
        }

        var userToUpdate = userOptional.get();
        try{
            var updatedUser= userRepository.save(userToUpdate.updateStudentDetails(updateUserCommand));
            return Optional.of(updatedUser);
        }catch (Exception e) {
            // Handle exception, e.g., log it or rethrow as a custom exception
            return Optional.empty();
        }
    }

    @Override
    public void handle(DeleteUserCommand deleteUserCommand) {
        if (!userRepository.existsById(deleteUserCommand.userId())) {
            throw new IllegalArgumentException("User with ID " + deleteUserCommand.userId() + " not found");
        }

        try{
            userRepository.deleteById(deleteUserCommand.userId());
        } catch (Exception e) {
            // Handle exception, e.g., log it or rethrow as a custom exception
            throw new RuntimeException("Error deleting user: " + e.getMessage(), e);
        }
    }

    @Override
    public Optional<User> handle(LeaveGroupCommand leaveGroupCommand) {
        // Check if the user exists
        var userOptional = userRepository.findById(leaveGroupCommand.userId());
        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("User with ID " + leaveGroupCommand.userId() + " not found");
        }

        // Check if the group ID is valid
        var user = userOptional.get();
        user.removeFromGroup(leaveGroupCommand.groupId());

        // Save the updated user
        try {
            userRepository.save(user);
            return Optional.of(user);
        } catch (Exception e) {
            throw new RuntimeException("Error while removing user from group", e);
        }


    }

    @Override
    public Optional<ImmutablePair<User, String>> handle(SignInCommand signInCommand) {
        var user = userRepository.findByEmail(signInCommand.email());

        if (user.isEmpty()) {
            throw new IllegalArgumentException("User with email " + signInCommand.email() + " not found");
        }
        if (!hashingService.matches(signInCommand.password(), user.get().getPassword())) {
            throw new IllegalArgumentException("Invalid password");
        }
        var token = tokenService.generateToken(user.get().getEmail());
        return Optional.of(ImmutablePair.of(user.get(), token));
    }

    @Override
    public Optional<User> handle(SignUpCommand signUpCommand) {
        if (userRepository.existsByEmail(signUpCommand.email())) {
            throw new IllegalArgumentException("User with email " + signUpCommand.email() + " already exists");
        }
        var roles= signUpCommand.roles().stream().map(
                role->roleRepository.findByName(role)
                        .orElseThrow(() -> new IllegalArgumentException("Role " + role + " not found"))
                ).toList();
        var user = new User(signUpCommand.email(), hashingService.encode(signUpCommand.password()), roles);
        userRepository.save(user);
        return userRepository.findByEmail(signUpCommand.email());
    }
}
