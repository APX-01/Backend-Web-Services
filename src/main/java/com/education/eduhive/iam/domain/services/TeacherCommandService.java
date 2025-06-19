package com.education.eduhive.iam.domain.services;

import com.education.eduhive.iam.domain.model.aggregates.Teacher;
import com.education.eduhive.iam.domain.model.commads.CreateTeacherCommand;
import com.education.eduhive.iam.domain.model.commads.DeleteTeacherCommand;
import com.education.eduhive.iam.domain.model.commads.UpdateTeacherCommand;

import java.util.Optional;

public interface TeacherCommandService {

    Optional<Teacher> handle(CreateTeacherCommand createTeacherCommand);

    Optional<Teacher> handle(UpdateTeacherCommand updateTeacherCommand);

    void handle(DeleteTeacherCommand deleteTeacherCommand);
}
