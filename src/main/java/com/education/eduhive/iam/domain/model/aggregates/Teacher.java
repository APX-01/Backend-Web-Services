package com.education.eduhive.iam.domain.model.aggregates;

import com.education.eduhive.iam.domain.model.commads.CreateTeacherCommand;
import com.education.eduhive.iam.domain.model.commads.UpdateStudentCommand;
import com.education.eduhive.iam.domain.model.commads.UpdateTeacherCommand;
import com.education.eduhive.iam.domain.model.valueobjects.ProfileInGroup;
import com.education.eduhive.iam.domain.model.valueobjects.Role;
import com.education.eduhive.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import com.education.eduhive.submissions.domain.model.commands.CreateSubmissionCommand;
import com.education.eduhive.submissions.domain.model.valueobjects.ChallengeId;
import com.education.eduhive.submissions.domain.model.valueobjects.Content;
import com.education.eduhive.submissions.domain.model.valueobjects.Score;
import com.education.eduhive.submissions.domain.model.valueobjects.StudentId;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
public class Teacher extends AuditableAbstractAggregateRoot<Teacher> {

    private String email;
    private String firstName;
    private String lastName;

    @Enumerated(EnumType.STRING)
    private Role role;

    private String password;

    @ElementCollection
    @CollectionTable(name = "teacher_profiles_in_groups", joinColumns = @JoinColumn(name = "teacher_id"))
    private List<ProfileInGroup> profilesInGroups = new ArrayList<>();

    protected Teacher() {
        super();
    }

    //command
    public Teacher(CreateTeacherCommand createTeacherCommand) {
        super();
        this.email = createTeacherCommand.email();
        this.firstName = createTeacherCommand.firstName();
        this.lastName = createTeacherCommand.lastName();
        this.role = Role.ROLE_TEACHER; // Assuming the role is always TEACHER for this command
        this.password = createTeacherCommand.password();
        this.profilesInGroups = new ArrayList<>(); // Initialize with an empty list
    }

    //update
    public Teacher updateTeacherDetails(UpdateTeacherCommand updateTeacherCommand){
        this.email = updateTeacherCommand.email();
        this.firstName = updateTeacherCommand.firstName();
        this.lastName = updateTeacherCommand.lastName();
        // Assuming the role remains the same, we do not change it here
        this.password = updateTeacherCommand.password();
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
