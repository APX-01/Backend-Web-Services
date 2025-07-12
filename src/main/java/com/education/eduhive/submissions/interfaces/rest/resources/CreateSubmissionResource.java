package com.education.eduhive.submissions.interfaces.rest.resources;

public record CreateSubmissionResource(
        Long challengeId,
        String content,
        String imageUrl) {

    public CreateSubmissionResource{
        if (challengeId == null || challengeId <= 0) {
            throw new IllegalArgumentException("Challenge ID must be greater than 0");
        }
//        if (studentId == null || studentId <= 0) {
//            throw new IllegalArgumentException("Student ID must be greater than 0");
//        }
        if (content == null || content.isBlank()) {
            throw new IllegalArgumentException("Content cannot be null or blank");
        }
//        if (score < 0 || score > 20) {
//            throw new IllegalArgumentException("Score must be between 0 and 20");
//        }
        if (imageUrl == null || imageUrl.isBlank()) {
            throw new IllegalArgumentException("Image URL cannot be null or blank");
        }
    }

}
