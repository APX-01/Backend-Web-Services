package com.education.eduhive.iam.application.internal.queryservices;

import com.education.eduhive.iam.domain.model.aggregates.Student;
import com.education.eduhive.iam.domain.model.queries.GetAllStudentsQuery;
import com.education.eduhive.iam.domain.model.queries.GetStudentByIdQuery;
import com.education.eduhive.iam.domain.services.StudentQueryService;
import com.education.eduhive.iam.infrastructure.persistence.jpa.repositories.StudentRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class StudentQueryServiceImpl implements StudentQueryService {

    private final StudentRepository studentRepository;

    public StudentQueryServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public Optional<Student> handle(GetStudentByIdQuery getStudentByIdQuery) {
        return studentRepository.findById(getStudentByIdQuery.studentId());
    }

    @Override
    public List<Student> handle(GetAllStudentsQuery getAllStudentsQuery) {
        return studentRepository.findAll();
    }
}
