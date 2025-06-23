package com.education.eduhive.groups.application.internal.queryservices;

import com.education.eduhive.groups.domain.model.aggregates.Group;
import com.education.eduhive.groups.domain.model.queries.*;
import com.education.eduhive.groups.domain.model.valueobjects.GroupJoinCode;
import com.education.eduhive.groups.domain.services.GroupQueryService;
import com.education.eduhive.groups.infrastructure.persistence.jpa.repositories.GroupRepository;
import com.education.eduhive.iam.domain.model.valueobjects.ProfileInGroup;
import com.education.eduhive.iam.infrastructure.persistence.jpa.repositories.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GroupQueryServiceImpl implements GroupQueryService {

    private final GroupRepository groupRepository;
    private final UserRepository userRepository;

    public GroupQueryServiceImpl(GroupRepository groupRepository,UserRepository userRepository) {
        this.groupRepository = groupRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Group> handle(GetAllGroupsQuery query) {
        return groupRepository.findAll();
    }

    @Override
    public Optional<Group> handle(GetGroupByIdQuery query) {
        return groupRepository.findById(query.id());
    }

    @Override
    public Optional<GroupJoinCode> handle(GetGroupJoinCodeByGroupId query) {
        var group = groupRepository.findById(query.groupId());

        return group.map(Group::getJoinCode);
    }

    @Override
    public List<Group> handle(GetGroupsByUserIdQuery getGroupsByUserIdQuery) {

        // Validate that the userId is not null or empty
        var optionalUser= userRepository.findById(getGroupsByUserIdQuery.userId());

        if(optionalUser.isEmpty()){
            throw new IllegalArgumentException("User with ID " + getGroupsByUserIdQuery.userId() + " not found");
        }

        // If the user exists, proceed to get the groups
        var userIds = optionalUser.get().getProfilesInGroups().stream()
                .map(ProfileInGroup::getGroupId)
                .toList();

        return groupRepository.findAllByIdIn(userIds);
    }

    @Override
    public Optional<Group> handle(GetGroupByKeyQuery getGroupByKeyQuery) {
        return groupRepository.findByJoinCode_Key(getGroupByKeyQuery.key());
    }


}
