package com.education.eduhive.submissions.domain.services;

import com.education.eduhive.submissions.domain.model.aggregates.Submission;
import com.education.eduhive.submissions.domain.model.commands.CreateSubmissionCommand;
import com.education.eduhive.submissions.domain.model.commands.DeleteSubmissionCommand;
import com.education.eduhive.submissions.domain.model.commands.GradeSubmissionCommand;
import com.education.eduhive.submissions.domain.model.commands.UpdateSubmissionCommand;

import java.util.Optional;

public interface SubmissionCommandService {


    Long handle(CreateSubmissionCommand createSubmissionCommand);
    //Porque se espera que, después de crear el submission, se devuelva su ID generado

    Optional<Submission> handle(UpdateSubmissionCommand updateSubmissionCommand);
    //¿Por qué retorna Optional<Course>? Porque puede que el curso no exista.
    //Si lo encuentra y actualiza → devuelve el Course.
    //Si no lo encuentra → devuelve Optional.empty().

    void handle(DeleteSubmissionCommand deleteSubmissionCommand);
    //¿Por qué retorna void? Porque no se necesita retornar nada.

    Optional<Submission> handle(GradeSubmissionCommand command);
}
