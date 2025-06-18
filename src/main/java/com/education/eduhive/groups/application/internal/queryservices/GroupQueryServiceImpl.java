package com.education.eduhive.groups.application.internal.queryservices;

import com.education.eduhive.groups.domain.model.aggregates.Group;
import com.education.eduhive.groups.domain.model.queries.GetAllGroupsQuery;
import com.education.eduhive.groups.domain.model.queries.GetGroupByIdQuery;
import com.education.eduhive.groups.domain.services.GroupQueryService;
import com.education.eduhive.groups.infrastructure.persistence.jpa.repositories.GroupRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GroupQueryServiceImpl implements GroupQueryService {

    private final GroupRepository groupRepository;

    public GroupQueryServiceImpl(GroupRepository groupRepository) {
        this.groupRepository = groupRepository;
    }

    @Override
    public List<Group> handle(GetAllGroupsQuery query) {
        return groupRepository.findAll();
    }

    @Override
    public Optional<Group> handle(GetGroupByIdQuery query) {
        return groupRepository.findById(query.id());
    }
}
