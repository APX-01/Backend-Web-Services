package com.education.eduhive.challenges.domain.model.valueobjects;

import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import java.util.Date;

public record Deadline(@Temporal(TemporalType.TIMESTAMP)Date deadline) {

}
