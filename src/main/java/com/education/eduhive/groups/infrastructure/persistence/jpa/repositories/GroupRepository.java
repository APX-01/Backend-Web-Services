package com.education.eduhive.groups.infrastructure.persistence.jpa.repositories;

import com.education.eduhive.groups.domain.model.aggregates.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

}
