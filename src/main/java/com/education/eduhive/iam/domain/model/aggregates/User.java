package com.education.eduhive.iam.domain.model.aggregates;

import com.education.eduhive.iam.domain.model.commads.CreateUserCommand;
import com.education.eduhive.iam.domain.model.commads.UpdateUserCommand;
import com.education.eduhive.iam.domain.model.entities.Role;
import com.education.eduhive.iam.domain.model.valueobjects.ProfileInGroup;
import com.education.eduhive.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Entity
public class User extends AuditableAbstractAggregateRoot<User> {

    private String email;
    private String firstName;
    private String lastName;

    //@Enumerated(EnumType.STRING)
    //private Roles roles;

    @ManyToMany(fetch = FetchType.EAGER,cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name="user_id"),
            inverseJoinColumns = @JoinColumn(name="role_id")
    )
    private Set<Role> roles;

    private String password;

    @ElementCollection
    @CollectionTable(name = "user_profiles_in_groups", joinColumns = @JoinColumn(name = "user_id"))
    private List<ProfileInGroup> profilesInGroups = new ArrayList<>();

    protected User() {
        super();
        this.roles = new HashSet<>(); // Initialize with an empty set
        this.profilesInGroups = new ArrayList<>(); // Initialize with an empty list
    }

    //command
//    public User(CreateUserCommand createUserCommand){
//        super();
//        this.email = createUserCommand.email();
//        this.firstName = createUserCommand.firstName();
//        this.lastName = createUserCommand.lastName();
//        this.roles = Role.validateRoleSet(createUserCommand.roles()); // Assuming the role is always STUDENT for this command
//        this.password = createUserCommand.password();
//        this.profilesInGroups = new ArrayList<>(); // Initialize with an empty list
//    }

    public User(String email, String password) {
        this.email = email;
        this.password = password;
        this.roles = new HashSet<>();
    }

    public User(String email, String password, List<Role> roles) {
        this(email, password);
        addRoles(roles);
    }


    //update
    public User updateStudentDetails(UpdateUserCommand updateUserCommand){
        this.email = updateUserCommand.email();
        this.firstName = updateUserCommand.firstName();
        this.lastName = updateUserCommand.lastName();
        // Assuming the role remains the same, we do not change it here
        this.password = updateUserCommand.password();
        // If you need to update profilesInGroups, you can add logic here
        return this;
    }

    // additional constructor for updating teacher details
    public void assignToGroup(Long groupId) {
        this.profilesInGroups.add(new ProfileInGroup(groupId, 0));
    }

    public void removeFromGroup(Long groupId) {
        this.profilesInGroups.removeIf(p -> p.getGroupId().equals(groupId));
    }

    // Methods to manage roles
    public User addRole(Role role) {
        this.roles.add(role);
        return this;
    }

    public User removeRole(Role role) {
        this.roles.remove(role);
        return this;
    }

    public void addRoles(List<Role> roles) {
        var validatedRoleSet = Role.validateRoleSet(roles);
        this.roles.addAll(validatedRoleSet);
    }

}
