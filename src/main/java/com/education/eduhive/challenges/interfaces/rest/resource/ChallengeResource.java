package com.education.eduhive.challenges.interfaces.rest.resource;

import java.util.Date;

public record ChallengeResource(Long id,String title, String description, Long groupId, Date deadline, String imageUrl) {
}
