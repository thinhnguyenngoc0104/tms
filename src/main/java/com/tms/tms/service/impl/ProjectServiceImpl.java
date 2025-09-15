package com.tms.tms.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

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
    public ProjectResponse add(ProjectRequest request) {
        ProjectEntity project = toEntity(request);
        // Find owner
        UserEntity owner = userRepository.findById(request.getOwnerId())
                .orElseThrow(() -> new EntityNotFoundException("Owner not found" + request.getOwnerId()));
        project.setOwner(owner);

        // Set ID
        // long counter = projectRepository.count() + 1;
        // project.setProjectId("P" + String.format("%03d", counter));

        project = projectRepository.save(project);
        return toResponse(project);
    }

    @Override
    public ProjectResponse update(ProjectRequest request, Long id) {
        ProjectEntity project = projectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Project not found" + id));
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project = projectRepository.save(project);
        return toResponse(project);
    }

    @Override
    public List<ProjectResponse> read() {
        return projectRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    public void delete(Long id) {
        ProjectEntity project = projectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Project not found: " + id));
        projectRepository.delete(project);
    }

    private ProjectResponse toResponse(ProjectEntity project) {
        return ProjectResponse.builder()
                .name(project.getName())
                .description(project.getDescription())
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
