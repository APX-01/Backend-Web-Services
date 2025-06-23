package com.education.eduhive.challenges.domain.model.aggregates;

import com.education.eduhive.challenges.domain.model.commands.CreateChallengeCommand;
import com.education.eduhive.challenges.domain.model.valueobjects.Deadline;
import com.education.eduhive.challenges.domain.model.valueobjects.Description;
import com.education.eduhive.challenges.domain.model.valueobjects.GroupId;
import com.education.eduhive.challenges.domain.model.valueobjects.Title;
import com.education.eduhive.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import lombok.Getter;

import java.util.Date;

@Getter
@Entity
public class Challenge extends AuditableAbstractAggregateRoot<Challenge> {

    @Embedded
    private Title title;

    @Embedded
    private Description description;

    @Embedded
    private GroupId groupId;

    @Embedded
    private Deadline deadline;

    private String imageUrl;

    protected Challenge() {
        super();
    }

    public Challenge( CreateChallengeCommand createChallengeCommand){
        this.title=new Title(createChallengeCommand.title());
        this.description=new Description(createChallengeCommand.description());
        this.groupId=new GroupId(createChallengeCommand.groupId());
        this.deadline=new Deadline(createChallengeCommand.deadline());
        this.imageUrl = createChallengeCommand.imageUrl();
    }

    public Challenge updateInformation(String title, String description, Long groupId, Date deadline, String imageUrl) {
        this.title=new Title(title);
        this.description=new Description(description);
        this.groupId=new GroupId(groupId);
        this.deadline=new Deadline(deadline);
        this.imageUrl = imageUrl;
        return this;
    }

}
