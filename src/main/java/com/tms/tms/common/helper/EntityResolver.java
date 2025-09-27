package com.tms.tms.common.helper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.tms.tms.entity.ProjectEntity;
import com.tms.tms.entity.TaskEntity;
import com.tms.tms.entity.UserEntity;
import com.tms.tms.io.ProjectMemberResponse;
import com.tms.tms.repository.ProjectRepository;
import com.tms.tms.repository.TaskRepository;
import com.tms.tms.repository.UserRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class EntityResolver {
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final TaskRepository taskRepository;

    public ProjectEntity getProjectOrThrow(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new EntityNotFoundException("Project not found: " + projectId));
    }

    public UserEntity getUserOrThrow(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + userId));
    }

    public TaskEntity getTaskOrThrow(Long taskId) {
        return taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found: " + taskId));
    }

    public UserEntity getUserBySubOrThrow(String sub) {
        return userRepository.findBySub(sub)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + sub));
    }

    public void validateMember(ProjectEntity project, Long userId) {
        boolean exists = project.getMembers().stream()
                .anyMatch(member -> member.getId().equals(userId));
        if (!exists) {
            throw new IllegalArgumentException("User is not a member of this project");
        }
    }

    public List<ProjectMemberResponse> findAndMapMembers(Long projectId) {
        return userRepository.findAllMembersByProjectId(projectId).stream()
                .map(member -> ProjectMemberResponse.builder()
                        .id(member.getId())
                        .name(member.getName())
                        .build())
                .toList();
    }
}
