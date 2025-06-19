package com.education.eduhive.iam.domain.services;

import com.education.eduhive.iam.domain.model.aggregates.Student;
import com.education.eduhive.iam.domain.model.queries.GetAllStudentsQuery;
import com.education.eduhive.iam.domain.model.queries.GetStudentByIdQuery;

import java.util.List;
import java.util.Optional;

public interface StudentQueryService {
    Optional<Student> handle(GetStudentByIdQuery getStudentByIdQuery);

    List<Student> handle(GetAllStudentsQuery getAllStudentsQuery);


}
