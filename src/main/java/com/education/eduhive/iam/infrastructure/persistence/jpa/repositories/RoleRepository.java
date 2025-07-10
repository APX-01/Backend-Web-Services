package com.education.eduhive.iam.infrastructure.persistence.jpa.repositories;

import com.education.eduhive.iam.domain.model.entities.Role;
import com.education.eduhive.iam.domain.model.valueobjects.Roles;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {

    Optional<Role> findByName(Roles name);

    boolean existsByName(Roles name);
}
