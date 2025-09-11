package com.tms.tms.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.tms.tms.entity.ProjectEntity;
import com.tms.tms.entity.TaskEntity;
import com.tms.tms.entity.UserEntity;
import com.tms.tms.io.TaskRequest;
import com.tms.tms.io.TaskResponse;
import com.tms.tms.repository.ProjectRepository;
import com.tms.tms.repository.TaskRepository;
import com.tms.tms.repository.UserRepository;
import com.tms.tms.service.TaskService;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class TaskServiceImpl implements TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    @Override
    @Transactional
    public TaskResponse add(TaskRequest request) {
        TaskEntity task = toEntity(request);

        // Find assignee
        UserEntity assignee = userRepository.findByUserId(request.getAssigneeId())
                .orElseThrow(() -> new EntityNotFoundException("Assignee not found" + request.getAssigneeId()));
        task.setAssignee(assignee);

        // Find assignee
        ProjectEntity project = projectRepository.findByProjectId(request.getProjectId())
                .orElseThrow(() -> new EntityNotFoundException("Project not found" + request.getProjectId()));
        task.setProject(project);

        // Set ID
        long counter = taskRepository.count() + 1;
        task.setTaskId("TASK" + String.format("%04d", counter));

        task = taskRepository.save(task);
        return toResponse(task);
    }

    @Override
    @Transactional
    public TaskResponse update(TaskRequest request, String taskId) {
        TaskEntity task = taskRepository.findByTaskId(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found" + taskId));
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus());
        task.setPriority(request.getPriority());
        task.setDueDate(request.getDueDate());
        task.setAssignee(userRepository.findByUserId(request.getAssigneeId())
                .orElseThrow(() -> new EntityNotFoundException("Assignee not found" + request.getAssigneeId())));
        task = taskRepository.save(task);
        return toResponse(task);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaskResponse> read() {
        return taskRepository.findAll().stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public void delete(String itemId) {
        TaskEntity task = taskRepository.findByTaskId(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found: " + itemId));
        taskRepository.delete(task);
    }

    private TaskResponse toResponse(TaskEntity task) {
        return TaskResponse.builder()
                .taskId(task.getTaskId())
                .title(task.getTitle())
                .description(task.getDescription())
                .status(task.getStatus())
                .priority(task.getPriority())
                .dueDate(task.getDueDate())
                .assigneeId(task.getAssignee().getUserId())
                .projectId(task.getProject().getProjectId())
                .build();
    }

    private TaskEntity toEntity(TaskRequest request) {
        return TaskEntity.builder()
                .title(request.getTitle())
                .description(request.getDescription())
                .status(request.getStatus())
                .priority(request.getPriority())
                .dueDate(request.getDueDate())
                .build();
    }

}
