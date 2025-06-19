package com.education.eduhive.iam.application.internal.commandservices;

import com.education.eduhive.iam.domain.model.aggregates.Student;
import com.education.eduhive.iam.domain.model.commads.CreateStudentCommand;
import com.education.eduhive.iam.domain.model.commads.DeleteStudentCommand;
import com.education.eduhive.iam.domain.model.commads.UpdateStudentCommand;
import com.education.eduhive.iam.domain.services.StudentCommandService;
import com.education.eduhive.iam.infrastructure.persistence.jpa.repositories.StudentRepository;
import com.education.eduhive.iam.infrastructure.persistence.jpa.repositories.TeacherRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class StudentCommandServiceImpl implements StudentCommandService {

    private final StudentRepository studentRepository;

    public StudentCommandServiceImpl(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    @Override
    public Optional<Student> handle(CreateStudentCommand createStudentCommand) {
        var student = new Student(createStudentCommand);

        try{
            studentRepository.save(student);
            return Optional.of(student);
        } catch (Exception e) {
            // Handle exception, e.g., log it or rethrow as a custom exception
            return Optional.empty();
        }
    }

    @Override
    public Optional<Student> handle(UpdateStudentCommand updateStudentCommand) {
        var studentOptional = studentRepository.findById(updateStudentCommand.studentId());
        if (studentOptional.isEmpty()) {
            throw new IllegalArgumentException("Student with ID " + updateStudentCommand.studentId() + " not found");
        }

        var studentToUpdate = studentOptional.get();
        try{
            var updatedStudent=studentRepository.save(studentToUpdate.updateStudentDetails(updateStudentCommand));
            return Optional.of(updatedStudent);
        }catch (Exception e) {
            // Handle exception, e.g., log it or rethrow as a custom exception
            return Optional.empty();
        }
    }

    @Override
    public void handle(DeleteStudentCommand deleteStudentCommand) {
        if (!studentRepository.existsById(deleteStudentCommand.studentId())) {
            throw new IllegalArgumentException("Student with ID " + deleteStudentCommand.studentId() + " not found");
        }

        try{
            studentRepository.deleteById(deleteStudentCommand.studentId());
        } catch (Exception e) {
            // Handle exception, e.g., log it or rethrow as a custom exception
            throw new RuntimeException("Error deleting student: " + e.getMessage(), e);
        }
    }
}
