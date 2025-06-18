package com.education.eduhive.groups.domain.services;

import com.education.eduhive.groups.domain.model.aggregates.Group;
import com.education.eduhive.groups.domain.model.queries.GetAllGroupsQuery;
import com.education.eduhive.groups.domain.model.queries.GetGroupByIdQuery;

import java.util.List;
import java.util.Optional;

public interface GroupQueryService {

    List<Group> handle(GetAllGroupsQuery query);

    Optional<Group> handle(GetGroupByIdQuery query);

}
