package com.tms.tms.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tms.tms.common.helper.CurrentUserProvider;
import com.tms.tms.common.mapper.ProjectMapper;
import com.tms.tms.common.mapper.TaskMapper;
import com.tms.tms.entity.ProjectEntity;
import com.tms.tms.entity.UserEntity;
import com.tms.tms.io.ProjectRequest;
import com.tms.tms.io.ProjectResponse;
import com.tms.tms.io.ProjectMemberResponse;
import com.tms.tms.io.TaskResponse;
import com.tms.tms.repository.ProjectRepository;
import com.tms.tms.repository.UserRepository;
import com.tms.tms.service.ProjectService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ProjectServiceImpl implements ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final ProjectMapper projectMapper;
    private final TaskMapper taskMapper;
    private final CurrentUserProvider currentUserProvider;

    @Override
    public ProjectResponse add(ProjectRequest request) {
        ProjectEntity project = projectMapper.toEntity(request);

        String currentSub = currentUserProvider.getCurrentUserSub();
        UserEntity owner = userRepository.findBySub(currentSub)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));
        project.setOwner(owner);

        project = projectRepository.save(project);
        return projectMapper.toResponse(project);
    }

    @Override
    public ProjectResponse update(ProjectRequest request, Long id) {
        ProjectEntity project = projectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Project not found" + id));
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project = projectRepository.save(project);
        return projectMapper.toResponse(project);
    }

    @Override
    public List<ProjectResponse> read() {
        return projectRepository.findAll().stream()
                .map(projectMapper::toResponse)
                .toList();
    }

    @Override
    public ProjectResponse findById(Long id) {
        ProjectEntity project = projectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Project not found: " + id));
        return projectMapper.toResponse(project);
    }

    @Override
    public void delete(Long id) {
        ProjectEntity project = projectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Project not found: " + id));
        projectRepository.delete(project);
    }

    @Override
    public List<TaskResponse> getProjectTasks(Long projectId) {
        ProjectEntity project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found: " + projectId));

        return project.getTasks().stream()
                .map(taskMapper::toResponse)
                .toList();
    }

    @Override
    public List<ProjectMemberResponse> getProjectMembers(Long projectId) {
        ProjectEntity project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found: " + projectId));

        // TODO: Inject mapper
        return project.getMembers().stream()
                .map(member -> ProjectMemberResponse.builder()
                        .id(member.getId())
                        .name(member.getName())
                        .build())
                .toList();
    }

    @Override
    public void addMemberToProject(Long projectId, Long userId) {
        ProjectEntity project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found: " + projectId));

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + userId));

        // Check if user is already a member
        if (project.getMembers().stream().anyMatch(member -> member.getId().equals(userId))) {
            throw new IllegalArgumentException("User is already a member of this project");
        }

        project.getMembers().add(user);
        projectRepository.save(project);
    }

    @Override
    public void removeMemberFromProject(Long projectId, Long userId) {
        ProjectEntity project = projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found: " + projectId));

        // Check if user is a member
        boolean removed = project.getMembers().removeIf(member -> member.getId().equals(userId));

        if (!removed) {
            throw new IllegalArgumentException("User is not a member of this project");
        }

        project.getTasks().removeIf(task -> task.getAssignee() != null && task.getAssignee().getId().equals(userId));
        projectRepository.save(project);
    }
}
