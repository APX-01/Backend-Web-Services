package com.education.eduhive.iam.interfaces.rest.transform;

import com.education.eduhive.iam.domain.model.commads.UpdateStudentCommand;
import com.education.eduhive.iam.interfaces.rest.resources.UpdateStudentResource;

public class UpdateStudentCommandFromResourceAssembler {
    public static UpdateStudentCommand toCommandFromResource(Long studentId,UpdateStudentResource updateStudentResource){
        return new UpdateStudentCommand(
                studentId,
                updateStudentResource.email(),
                updateStudentResource.firstName(),
                updateStudentResource.lastName(),
                updateStudentResource.password());
    }
}
