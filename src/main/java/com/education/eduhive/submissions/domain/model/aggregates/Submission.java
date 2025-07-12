package com.education.eduhive.submissions.domain.model.aggregates;

import com.education.eduhive.shared.domain.model.aggregates.AuditableAbstractAggregateRoot;
import com.education.eduhive.submissions.domain.model.commands.CreateSubmissionCommand;
import com.education.eduhive.submissions.domain.model.valueobjects.*;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@Entity
public class Submission extends AuditableAbstractAggregateRoot<Submission> {


    //id en el auditable abstract aggregate root

    @Embedded
    private ChallengeId challengeId;

    @Embedded
    private StudentId studentId;

    @Embedded
    private Content content;

    @Embedded
    private Score score;

    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private States state;

    protected Submission() {
        super();
    }

    /*
    public Submission(Long challengeId, Long studentId, String content, int score, String imageUrl) {
        this.challengeId = new ChallengeId(challengeId);
        this.studentId = new StudentId(studentId);
        this.content = new Content(content);
        this.score = new Score(score);
        this.imageUrl = imageUrl;
    }
    */

    public Submission(CreateSubmissionCommand command) {
        this.challengeId = new ChallengeId(command.challengeId());
        this.studentId = new StudentId(command.studentId());
        this.content = new Content(command.content());
        this.score = new Score(0);
        this.imageUrl = command.imageUrl();
        this.state = States.NOT_GRADED; // Estado inicial
    }

    //Metodos que permiten actualizar el contenido y la puntuación de la submission

    public Submission updateSubmission(Long newChallengeId, Long newStudentId, String newContent, int newScore, String newImageUrl) {
        this.challengeId = new ChallengeId(newChallengeId);
        this.studentId = new StudentId(newStudentId);
        this.content = new Content(newContent);
        this.score = new Score(newScore);
        this.imageUrl = newImageUrl;
        return this;
    }

    public Submission gradeSubmission(int newScore) {
        this.score = new Score(newScore);
        return this;
    }

    public Submission changeState(States newState) {
        this.state = newState;
        return this;
    }


}
