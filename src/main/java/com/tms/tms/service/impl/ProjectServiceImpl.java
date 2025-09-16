package com.tms.tms.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tms.tms.common.mapper.ProjectMapper;
import com.tms.tms.common.mapper.TaskMapper;
import com.tms.tms.entity.ProjectEntity;
import com.tms.tms.entity.UserEntity;
import com.tms.tms.io.ProjectRequest;
import com.tms.tms.io.ProjectResponse;
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

    @Override
    public ProjectResponse add(ProjectRequest request) {
        ProjectEntity project = ProjectMapper.toEntity(request);
        // Find owner
        UserEntity owner = userRepository.findById(request.getOwnerId())
                .orElseThrow(() -> new EntityNotFoundException("Owner not found" + request.getOwnerId()));
        project.setOwner(owner);

        project = projectRepository.save(project);
        return ProjectMapper.toResponse(project);
    }

    @Override
    public ProjectResponse update(ProjectRequest request, Long id) {
        ProjectEntity project = projectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Project not found" + id));
        project.setName(request.getName());
        project.setDescription(request.getDescription());
        project = projectRepository.save(project);
        return ProjectMapper.toResponse(project);
    }

    @Override
    public List<ProjectResponse> read() {
        return projectRepository.findAll().stream()
                .map(ProjectMapper::toResponse)
                .toList();
    }

    @Override
    public ProjectResponse findById(Long id) {
        ProjectEntity project = projectRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Project not found: " + id));
        return ProjectMapper.toResponse(project);
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
                .map(TaskMapper::toResponse)
                .toList();
    }
}
