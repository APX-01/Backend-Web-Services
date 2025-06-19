package com.education.eduhive.iam.infrastructure.persistence.jpa.repositories;

import com.education.eduhive.iam.domain.model.aggregates.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
