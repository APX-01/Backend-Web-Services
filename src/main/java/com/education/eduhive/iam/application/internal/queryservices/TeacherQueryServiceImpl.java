package com.education.eduhive.iam.application.internal.queryservices;

import com.education.eduhive.iam.domain.model.aggregates.Teacher;
import com.education.eduhive.iam.domain.model.queries.GetAllTeachersQuery;
import com.education.eduhive.iam.domain.model.queries.GetTeacherByIdQuery;
import com.education.eduhive.iam.domain.services.TeacherQueryService;
import com.education.eduhive.iam.infrastructure.persistence.jpa.repositories.StudentRepository;
import com.education.eduhive.iam.infrastructure.persistence.jpa.repositories.TeacherRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TeacherQueryServiceImpl implements TeacherQueryService {

    private final TeacherRepository teacherRepository;

    public TeacherQueryServiceImpl(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    @Override
    public Optional<Teacher> handle(GetTeacherByIdQuery getTeacherByIdQuery) {
        return teacherRepository.findById(getTeacherByIdQuery.teacherId());
    }

    @Override
    public List<Teacher> handle(GetAllTeachersQuery getAllTeachersQuery) {
        return teacherRepository.findAll();
    }
}
