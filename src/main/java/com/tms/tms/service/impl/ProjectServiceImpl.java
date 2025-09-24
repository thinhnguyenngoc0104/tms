package com.tms.tms.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tms.tms.common.helper.CurrentUserProvider;
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
import com.tms.tms.repository.UserRepository;
import com.tms.tms.service.AuthorizationService;
import com.tms.tms.service.ProjectService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

        private final ProjectRepository projectRepository;
        private final UserRepository userRepository;
        private final TaskRepository taskRepository;
        private final AppMapper appMapper;
        private final CurrentUserProvider currentUserProvider;
        private final AuthorizationService authorizationService;

        @Override
        @Transactional
        public ProjectResponse add(ProjectRequest request) {
                ProjectEntity project = appMapper.toProjectEntity(request);
                String currentSub = currentUserProvider.getCurrentUserSub();
                UserEntity owner = getUserBySubOrThrow(currentSub);
                project.setOwner(owner);
                project = projectRepository.save(project);
                return appMapper.toProjectResponse(project);
        }

        @Override
        @Transactional
        public ProjectResponse update(ProjectRequest request, Long id) {
                ProjectEntity project = getProjectOrThrow(id);
                project.setName(request.getName());
                project.setDescription(request.getDescription());
                project = projectRepository.save(project);
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
                ProjectEntity project = getProjectOrThrow(id);
                return appMapper.toProjectResponse(project);
        }

        @Override
        @Transactional
        public void delete(Long id) {
                ProjectEntity project = getProjectOrThrow(id);
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
                return userRepository.findAllMembersByProjectId(projectId).stream()
                                .map(member -> ProjectMemberResponse.builder()
                                                .id(member.getId())
                                                .name(member.getName())
                                                .build())
                                .toList();
        }

        @Override
        @Transactional
        public void addMemberToProject(Long projectId, Long userId) {
                ProjectEntity project = getProjectOrThrow(projectId);
                UserEntity user = getUserOrThrow(userId);
                validateMember(project, userId);
                project.getMembers().add(user);
                projectRepository.save(project);
        }

        @Override
        @Transactional
        public void removeMemberFromProject(Long projectId, Long userId) {
                ProjectEntity project = getProjectOrThrow(projectId);
                validateMember(project, userId);
                project.getTasks().removeIf(
                                task -> task.getAssignee() != null && task.getAssignee().getId().equals(userId));
                projectRepository.save(project);
        }

        // --- Helper methods ---
        private ProjectEntity getProjectOrThrow(Long projectId) {
                return projectRepository.findById(projectId)
                                .orElseThrow(() -> new EntityNotFoundException("Project not found: " + projectId));
        }

        private UserEntity getUserOrThrow(Long userId) {
                return userRepository.findById(userId)
                                .orElseThrow(() -> new EntityNotFoundException("User not found: " + userId));
        }

        private UserEntity getUserBySubOrThrow(String sub) {
                return userRepository.findBySub(sub)
                                .orElseThrow(() -> new EntityNotFoundException("User not found: " + sub));
        }

        private void validateMember(ProjectEntity project, Long userId) {
                boolean exists = project.getMembers().stream()
                                .anyMatch(member -> member.getId().equals(userId));
                if (!exists) {
                        throw new IllegalArgumentException("User is not a member of this project");
                }
        }
}
