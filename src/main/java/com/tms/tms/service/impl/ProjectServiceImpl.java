package com.tms.tms.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tms.tms.entity.ProjectEntity;
import com.tms.tms.entity.UserEntity;
import com.tms.tms.io.ProjectRequest;
import com.tms.tms.io.ProjectResponse;
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

    @Override
    @Transactional
    public ProjectResponse add(ProjectRequest request) {
        ProjectEntity project = toEntity(request);
        // Find owner
        UserEntity owner = userRepository.findByUserId(request.getOwnerId())
                .orElseThrow(() -> new EntityNotFoundException("Owner not found" + request.getOwnerId()));
        project.setOwner(owner);

        // Set ID
        long counter = projectRepository.count() + 1;
        project.setProjectId("PROJ" + String.format("%04d", counter));

        project = projectRepository.save(project);
        return toResponse(project);
    }

    @Override
    @Transactional
    public ProjectResponse update(ProjectRequest request, String projectId) {
        ProjectEntity project = projectRepository.findByProjectId(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found" + projectId));
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project = projectRepository.save(project);
        return toResponse(project);
    }

    @Override
    @Transactional(readOnly = true)
    public List<ProjectResponse> read() {
        return projectRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public void delete(String projectId) {
        ProjectEntity project = projectRepository.findByProjectId(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found: " + projectId));
        projectRepository.delete(project);
    }

    private ProjectResponse toResponse(ProjectEntity project) {
        return ProjectResponse.builder()
                .projectId(project.getProjectId())
                .name(project.getName())
                .description(project.getDescription())
                .ownerId(project.getOwner().getUserId())
                .createdAt(project.getCreatedAt())
                .updatedAt(project.getUpdatedAt())
                .taskCount(project.getTasks().size())
                .memberCount(project.getMembers().size())
                .build();
    }

    private ProjectEntity toEntity(ProjectRequest request) {
        return ProjectEntity.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();
    }
}
