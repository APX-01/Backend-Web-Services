package com.education.eduhive.iam.domain.model.aggregates;

import com.education.eduhive.iam.domain.model.commads.CreateStudentCommand;
import com.education.eduhive.iam.domain.model.commads.UpdateStudentCommand;
import com.education.eduhive.iam.domain.model.valueobjects.ProfileInGroup;
import com.education.eduhive.iam.domain.model.valueobjects.Role;
import com.education.eduhive.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
public class Student extends AuditableAbstractAggregateRoot<Student> {

    private String email;
    private String firstName;
    private String lastName;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String password;

    @ElementCollection
    @CollectionTable(name = "student_profiles_in_groups", joinColumns = @JoinColumn(name = "student_id"))
    private List<ProfileInGroup> profilesInGroups = new ArrayList<>();

    protected Student() {
        super();
    }

    //command
    public Student(CreateStudentCommand createStudentCommand){
        super();
        this.email = createStudentCommand.email();
        this.firstName = createStudentCommand.firstName();
        this.lastName = createStudentCommand.lastName();
        this.role = Role.ROLE_STUDENT; // Assuming the role is always STUDENT for this command
        this.password = createStudentCommand.password();
        this.profilesInGroups = new ArrayList<>(); // Initialize with an empty list
    }

    //update
    public Student updateStudentDetails(UpdateStudentCommand updateStudentCommand){
        this.email = updateStudentCommand.email();
        this.firstName = updateStudentCommand.firstName();
        this.lastName = updateStudentCommand.lastName();
        // Assuming the role remains the same, we do not change it here
        this.password = updateStudentCommand.password();
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


}
