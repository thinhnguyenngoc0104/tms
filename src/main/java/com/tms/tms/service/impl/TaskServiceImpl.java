package com.tms.tms.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tms.tms.common.mapper.AppMapper;
import com.tms.tms.entity.ProjectEntity;
import com.tms.tms.entity.TaskEntity;
import com.tms.tms.entity.UserEntity;
import com.tms.tms.io.TaskRequest;
import com.tms.tms.io.TaskResponse;
import com.tms.tms.io.TaskStatusUpdateRequest;
import com.tms.tms.repository.ProjectRepository;
import com.tms.tms.repository.TaskRepository;
import com.tms.tms.repository.UserRepository;
import com.tms.tms.service.AuthorizationService;
import com.tms.tms.service.TaskService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

        private final TaskRepository taskRepository;
        private final UserRepository userRepository;
        private final ProjectRepository projectRepository;
        private final AppMapper appMapper;
        private final AuthorizationService authorizationService;

        @Override
        @Transactional
        public TaskResponse add(TaskRequest request) {
                authorizationService.requireProjectAccess(request.getProjectId());
                TaskEntity task = appMapper.toTaskEntity(request);
                // Find assignee
                UserEntity assignee = getAssigneeOrThrow(request.getAssigneeId());
                task.setAssignee(assignee);
                // Find project
                ProjectEntity project = getProjectOrThrow(request.getProjectId());
                task.setProject(project);

                task = taskRepository.save(task);
                return appMapper.toTaskResponse(task);
        }

        @Override
        @Transactional
        public TaskResponse update(TaskRequest request, Long taskId) {
                TaskEntity task = getTaskOrThrow(taskId);
                authorizationService.requireProjectAccess(task.getProject().getId());

                task.setTitle(request.getTitle());
                task.setDescription(request.getDescription());
                task.setStatus(request.getStatus());
                task.setPriority(request.getPriority());
                task.setAssignee(getAssigneeOrThrow(request.getAssigneeId()));

                return appMapper.toTaskResponse(task);
        }

        @Override
        @Transactional(readOnly = true)
        public TaskResponse findById(Long id) {
                TaskEntity task = getTaskOrThrow(id);
                authorizationService.requireProjectAccess(task.getProject().getId());
                return appMapper.toTaskResponse(task);
        }

        @Override
        @Transactional
        public TaskResponse updateStatus(Long id, TaskStatusUpdateRequest request) {
                TaskEntity task = getTaskOrThrow(id);
                authorizationService.requireProjectAccess(task.getProject().getId());
                task.setStatus(request.getStatus());
                return appMapper.toTaskResponse(task);
        }

        @Override
        @Transactional
        public void delete(Long id) {
                TaskEntity task = getTaskOrThrow(id);
                authorizationService.requireProjectAccess(task.getProject().getId());
                taskRepository.delete(task);
        }

        // --- Helper methods ---
        private ProjectEntity getProjectOrThrow(Long projectId) {
                return projectRepository.findById(projectId)
                                .orElseThrow(() -> new EntityNotFoundException("Project not found: " + projectId));
        }

        private UserEntity getAssigneeOrThrow(Long userId) {
                return userRepository.findById(userId)
                                .orElseThrow(() -> new EntityNotFoundException("Assignee not found: " + userId));
        }

        private TaskEntity getTaskOrThrow(Long taskId) {
                return taskRepository.findById(taskId)
                                .orElseThrow(() -> new EntityNotFoundException("Task not found: " + taskId));
        }
}
