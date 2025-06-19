package com.education.eduhive.iam.interfaces.rest.transform;

import com.education.eduhive.iam.domain.model.commads.UpdateTeacherCommand;
import com.education.eduhive.iam.interfaces.rest.resources.UpdateTeacherResource;

public class UpdateTeacherCommandFromResourceAssembler {
    public static UpdateTeacherCommand toCommandFromResource(Long teacherId,UpdateTeacherResource updateTeacherResource){
        return new UpdateTeacherCommand(
                teacherId,
                updateTeacherResource.email(),
                updateTeacherResource.firstName(),
                updateTeacherResource.lastName(),
                updateTeacherResource.password()
        );
    }
}
