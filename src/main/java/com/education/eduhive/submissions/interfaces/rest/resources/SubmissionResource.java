package com.education.eduhive.submissions.interfaces.rest.resources;

public record SubmissionResource(
        Long id,
        Long challengeId,
        Long studentId,
        String content,
        int score,
        String imageUrl) {

}
