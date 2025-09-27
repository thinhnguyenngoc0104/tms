package com.tms.tms.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tms.tms.common.helper.EntityResolver;
import com.tms.tms.common.mapper.AppMapper;
import com.tms.tms.entity.ProjectEntity;
import com.tms.tms.entity.TaskEntity;
import com.tms.tms.entity.UserEntity;
import com.tms.tms.io.TaskRequest;
import com.tms.tms.io.TaskResponse;
import com.tms.tms.io.TaskStatusUpdateRequest;
import com.tms.tms.repository.TaskRepository;
import com.tms.tms.service.AuthorizationService;
import com.tms.tms.service.TaskService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

        private final TaskRepository taskRepository;
        private final AppMapper appMapper;
        private final AuthorizationService authorizationService;
        private final EntityResolver entityResolver;

        @Override
        @Transactional
        public TaskResponse add(TaskRequest request) {
                authorizationService.requireProjectAccess(request.getProjectId());
                TaskEntity task = appMapper.toTaskEntity(request);
                // Find assignee
                UserEntity assignee = entityResolver.getUserOrThrow(request.getAssigneeId());
                task.setAssignee(assignee);
                // Find project
                ProjectEntity project = entityResolver.getProjectOrThrow(request.getProjectId());
                task.setProject(project);

                task = taskRepository.save(task);
                return appMapper.toTaskResponse(task);
        }

        @Override
        @Transactional
        public TaskResponse update(TaskRequest request, Long taskId) {
                TaskEntity task = entityResolver.getTaskOrThrow(taskId);
                authorizationService.requireProjectAccess(task.getProject().getId());

                task.setTitle(request.getTitle());
                task.setDescription(request.getDescription());
                task.setStatus(request.getStatus());
                task.setPriority(request.getPriority());
                task.setAssignee(entityResolver.getUserOrThrow(request.getAssigneeId()));

                return appMapper.toTaskResponse(task);
        }

        @Override
        @Transactional(readOnly = true)
        public TaskResponse findById(Long id) {
                TaskEntity task = entityResolver.getTaskOrThrow(id);
                authorizationService.requireProjectAccess(task.getProject().getId());
                return appMapper.toTaskResponse(task);
        }

        @Override
        @Transactional
        public TaskResponse updateStatus(Long id, TaskStatusUpdateRequest request) {
                TaskEntity task = entityResolver.getTaskOrThrow(id);
                authorizationService.requireProjectAccess(task.getProject().getId());
                task.setStatus(request.getStatus());
                return appMapper.toTaskResponse(task);
        }

        @Override
        @Transactional
        public void delete(Long id) {
                TaskEntity task = entityResolver.getTaskOrThrow(id);
                authorizationService.requireProjectAccess(task.getProject().getId());
                taskRepository.delete(task);
        }
}
