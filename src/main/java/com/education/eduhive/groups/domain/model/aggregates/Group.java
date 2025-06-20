package com.education.eduhive.groups.domain.model.aggregates;

import com.education.eduhive.groups.domain.model.commands.CreateGroupCommand;
import com.education.eduhive.groups.domain.model.commands.UpdateGroupCommand;
import com.education.eduhive.groups.domain.model.valueobjects.GroupJoinCode;
import com.education.eduhive.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.annotation.Nullable;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;

import java.util.Date;

@Entity
@Getter
@Table(name = "user_groups")
public class Group extends AuditableAbstractAggregateRoot<Group> {

    private String name;

    private String description;

    private String imageUrl;

    @Embedded
    private GroupJoinCode joinCode;

    protected Group() { super(); }

    public Group(CreateGroupCommand command) {
        this.name = command.name();
        this.description = command.description();
        this.imageUrl = command.imageUrl();
        this.joinCode = null;
    }

    public Group updateGroup(UpdateGroupCommand command) {
        this.name = command.name();
        this.description = command.description();
        this.imageUrl = command.imageUrl();

        return this;
    }

    public Group setJoinCode(GroupJoinCode joinCode) {
        this.joinCode = joinCode;
        return this;
    }

    public Group resetJoinCode() {
        this.joinCode = null;
        return this;
    }
}
