package com.tms.tms.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tms.tms.common.helper.EntityResolver;
import com.tms.tms.common.mapper.AppMapper;
import com.tms.tms.entity.ProjectEntity;
import com.tms.tms.entity.TaskEntity;
import com.tms.tms.entity.UserEntity;
import com.tms.tms.io.ProjectRequest;
import com.tms.tms.io.ProjectResponse;
import com.tms.tms.io.ProjectMemberResponse;
import com.tms.tms.io.TaskResponse;
import com.tms.tms.repository.ProjectRepository;
import com.tms.tms.repository.TaskRepository;
import com.tms.tms.security.CurrentUserProvider;
import com.tms.tms.service.AuthorizationService;
import com.tms.tms.service.ProjectService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

        private final ProjectRepository projectRepository;
        private final TaskRepository taskRepository;
        private final AppMapper appMapper;
        private final CurrentUserProvider currentUserProvider;
        private final AuthorizationService authorizationService;
        private final EntityResolver entityResolver;

        @Override
        @Transactional
        public ProjectResponse add(ProjectRequest request) {
                ProjectEntity project = appMapper.toProjectEntity(request);
                String currentSub = currentUserProvider.getCurrentUserSub();
                UserEntity owner = entityResolver.getUserBySubOrThrow(currentSub);
                project.setOwner(owner);
                project = projectRepository.save(project);
                return appMapper.toProjectResponse(project);
        }

        @Override
        @Transactional
        public ProjectResponse update(ProjectRequest request, Long id) {
                ProjectEntity project = entityResolver.getProjectOrThrow(id);
                project.setName(request.getName());
                project.setDescription(request.getDescription());
                return appMapper.toProjectResponse(project);
        }

        @Override
        @Transactional(readOnly = true)
        public List<ProjectResponse> read() {
                List<ProjectEntity> projects = authorizationService.isAdmin()
                                ? projectRepository.findAll()
                                : projectRepository.findAllByUserAccess(currentUserProvider.getCurrentUserSub());
                return appMapper.toProjectResponses(projects);
        }

        @Override
        @Transactional(readOnly = true)
        public ProjectResponse findById(Long id) {
                authorizationService.requireProjectAccess(id);
                ProjectEntity project = entityResolver.getProjectOrThrow(id);
                return appMapper.toProjectResponse(project);
        }

        @Override
        @Transactional
        public void delete(Long id) {
                ProjectEntity project = entityResolver.getProjectOrThrow(id);
                projectRepository.delete(project);
        }

        @Override
        @Transactional(readOnly = true)
        public List<TaskResponse> getProjectTasks(Long projectId) {
                authorizationService.requireProjectAccess(projectId);
                List<TaskEntity> entities = taskRepository.findAllByProjectId(projectId);
                return appMapper.toTaskResponses(entities);
        }

        @Override
        @Transactional(readOnly = true)
        public List<ProjectMemberResponse> getProjectMembers(Long projectId) {
                authorizationService.requireProjectAccess(projectId);
                return entityResolver.findAndMapMembers(projectId);
        }

        @Override
        @Transactional
        public void addMemberToProject(Long projectId, Long userId) {
                ProjectEntity project = entityResolver.getProjectOrThrow(projectId);
                UserEntity user = entityResolver.getUserOrThrow(userId);
                entityResolver.validateMember(project, userId);
                project.getMembers().add(user);
        }

        @Override
        @Transactional
        public void removeMemberFromProject(Long projectId, Long userId) {
                ProjectEntity project = entityResolver.getProjectOrThrow(projectId);
                entityResolver.validateMember(project, userId);
                project.getTasks().removeIf(
                                task -> task.getAssignee() != null && task.getAssignee().getId().equals(userId));
        }
}
