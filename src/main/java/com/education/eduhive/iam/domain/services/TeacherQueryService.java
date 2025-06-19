package com.education.eduhive.iam.domain.services;

import com.education.eduhive.iam.domain.model.aggregates.Teacher;
import com.education.eduhive.iam.domain.model.queries.GetAllTeachersQuery;
import com.education.eduhive.iam.domain.model.queries.GetTeacherByIdQuery;

import java.util.List;
import java.util.Optional;

public interface TeacherQueryService {

    Optional<Teacher> handle(GetTeacherByIdQuery getTeacherByIdQuery);

    List<Teacher> handle(GetAllTeachersQuery getAllTeachersQuery);
}
