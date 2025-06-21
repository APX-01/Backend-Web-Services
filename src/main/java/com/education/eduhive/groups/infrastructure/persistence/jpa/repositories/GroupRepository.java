package com.education.eduhive.groups.infrastructure.persistence.jpa.repositories;

import com.education.eduhive.groups.domain.model.aggregates.Group;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {

    Optional<Group> findByJoinCode_Key(String keycode);

    List<Group> findAllByIdIn(List<Long> ids);

}
