package com.education.eduhive.iam.application.internal.commandservices;

import com.education.eduhive.iam.domain.model.aggregates.Teacher;
import com.education.eduhive.iam.domain.model.commads.CreateTeacherCommand;
import com.education.eduhive.iam.domain.model.commads.DeleteTeacherCommand;
import com.education.eduhive.iam.domain.model.commads.UpdateTeacherCommand;
import com.education.eduhive.iam.domain.services.TeacherCommandService;
import com.education.eduhive.iam.infrastructure.persistence.jpa.repositories.StudentRepository;
import com.education.eduhive.iam.infrastructure.persistence.jpa.repositories.TeacherRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TeacherCommandServiceImpl implements TeacherCommandService {

    private final TeacherRepository teacherRepository;

    public TeacherCommandServiceImpl(TeacherRepository teacherRepository) {
        this.teacherRepository = teacherRepository;
    }

    @Override
    public Optional<Teacher> handle(CreateTeacherCommand createTeacherCommand) {
        var teacher=new Teacher(createTeacherCommand);

        try{
            teacherRepository.save(teacher);
            return Optional.of(teacher);
        } catch (Exception e) {
            // Handle exception, e.g., log it or rethrow as a custom exception
            return Optional.empty();
        }
    }

    @Override
    public Optional<Teacher> handle(UpdateTeacherCommand updateTeacherCommand) {
        var teacherOptional = teacherRepository.findById(updateTeacherCommand.teacherId());
        if (teacherOptional.isEmpty()) {
            throw new IllegalArgumentException("Teacher with ID " + updateTeacherCommand.teacherId() + " not found");
        }

        var teacherToUpdate = teacherOptional.get();
        try{
            var updatedTeacher=teacherRepository.save(teacherToUpdate.updateTeacherDetails(updateTeacherCommand));
            return Optional.of(updatedTeacher);
        } catch (Exception e) {
            // Handle exception, e.g., log it or rethrow as a custom exception
            return Optional.empty();
        }
    }

    @Override
    public void handle(DeleteTeacherCommand deleteTeacherCommand) {
        if (!teacherRepository.existsById(deleteTeacherCommand.teacherId())) {
            throw new IllegalArgumentException("Teacher with ID " + deleteTeacherCommand.teacherId() + " not found");
        }

        try{
            teacherRepository.deleteById(deleteTeacherCommand.teacherId());
        } catch (Exception e) {
            // Handle exception, e.g., log it or rethrow as a custom exception
            throw new RuntimeException("Error deleting teacher: " + e.getMessage(), e);
        }

    }
}
