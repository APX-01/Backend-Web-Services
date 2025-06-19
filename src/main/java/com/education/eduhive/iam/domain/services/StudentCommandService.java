package com.education.eduhive.iam.domain.services;

import com.education.eduhive.iam.domain.model.aggregates.Student;
import com.education.eduhive.iam.domain.model.commads.CreateStudentCommand;
import com.education.eduhive.iam.domain.model.commads.DeleteStudentCommand;
import com.education.eduhive.iam.domain.model.commads.UpdateStudentCommand;

import java.util.Optional;

public interface StudentCommandService {

    Optional<Student> handle(CreateStudentCommand createStudentCommand);

    Optional<Student> handle(UpdateStudentCommand updateStudentCommand);

    void handle(DeleteStudentCommand deleteStudentCommand);
}
