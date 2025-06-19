package com.education.eduhive.iam.interfaces.rest.transform;

import com.education.eduhive.iam.domain.model.commads.CreateStudentCommand;
import com.education.eduhive.iam.interfaces.rest.resources.CreateStudentResource;
import com.education.eduhive.iam.interfaces.rest.resources.StudentResource;

public class CreateStudentCommandFromResourceAssembler {
    public static CreateStudentCommand toCommandFromResource(CreateStudentResource createStudentResource) {
        return new CreateStudentCommand(
                createStudentResource.email(),
                createStudentResource.firstName(),
                createStudentResource.lastName(),
                createStudentResource.password());
    }
}
