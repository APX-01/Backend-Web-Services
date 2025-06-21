package com.education.eduhive.groups.domain.services;

import com.education.eduhive.groups.domain.model.aggregates.Group;
import com.education.eduhive.groups.domain.model.queries.GetAllGroupsQuery;
import com.education.eduhive.groups.domain.model.queries.GetGroupByIdQuery;
import com.education.eduhive.groups.domain.model.queries.GetGroupJoinCodeByGroupId;
import com.education.eduhive.groups.domain.model.queries.GetGroupsByUserIdQuery;
import com.education.eduhive.groups.domain.model.valueobjects.GroupJoinCode;

import java.util.List;
import java.util.Optional;

public interface GroupQueryService {

    List<Group> handle(GetAllGroupsQuery query);

    Optional<Group> handle(GetGroupByIdQuery query);

    Optional<GroupJoinCode> handle(GetGroupJoinCodeByGroupId query);

    List<Group> handle(GetGroupsByUserIdQuery getGroupsByUserIdQuery);

}
