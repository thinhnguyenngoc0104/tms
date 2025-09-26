package com.tms.tms.common.mapper;

import org.mapstruct.*;

import com.tms.tms.entity.ProjectEntity;
import com.tms.tms.entity.TaskEntity;
import com.tms.tms.entity.UserEntity;
import com.tms.tms.io.ProjectRequest;
import com.tms.tms.io.ProjectResponse;
import com.tms.tms.io.TaskRequest;
import com.tms.tms.io.TaskResponse;
import com.tms.tms.io.UserResponse;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface AppMapper {

    // -------------------- User --------------------
    UserResponse toUserResponse(UserEntity entity);

    List<UserResponse> toUserResponses(List<UserEntity> entities);

    // -------------------- Task --------------------
    @Mapping(source = "assigneeId", target = "assignee.id")
    @Mapping(source = "projectId", target = "project.id")
    TaskEntity toTaskEntity(TaskRequest request);

    @Mapping(source = "assignee.id", target = "assigneeId")
    @Mapping(source = "assignee", target = "assignee")
    @Mapping(source = "project.id", target = "projectId")
    TaskResponse toTaskResponse(TaskEntity entity);

    List<TaskResponse> toTaskResponses(List<TaskEntity> entities);

    // -------------------- Project --------------------
    @Mapping(source = "ownerId", target = "owner.id")
    ProjectEntity toProjectEntity(ProjectRequest request);

    @Mapping(source = "owner.id", target = "ownerId")
    @Mapping(source = "owner", target = "owner")
    ProjectResponse toProjectResponse(ProjectEntity entity);

    List<ProjectResponse> toProjectResponses(List<ProjectEntity> entities);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "sub", ignore = true)
    void updateUserFromJwt(UserEntity source, @MappingTarget UserEntity target);
}
