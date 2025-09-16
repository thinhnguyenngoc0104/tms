package com.tms.tms.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.tms.tms.common.mapper.TaskMapper;
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
    public TaskResponse add(TaskRequest request) {
        TaskEntity task = TaskMapper.toEntity(request);

        // Find assignee
        UserEntity assignee = userRepository.findById(request.getAssigneeId())
                .orElseThrow(() -> new EntityNotFoundException("Assignee not found" + request.getAssigneeId()));
        task.setAssignee(assignee);

        // Find assignee
        ProjectEntity project = projectRepository.findById(request.getProjectId())
                .orElseThrow(() -> new EntityNotFoundException("Project not found" + request.getProjectId()));
        task.setProject(project);

        task = taskRepository.save(task);
        return TaskMapper.toResponse(task);
    }

    @Override
    public TaskResponse update(TaskRequest request, Long taskId) {
        TaskEntity task = taskRepository.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found" + taskId));
        task.setTitle(request.getTitle());
        task.setDescription(request.getDescription());
        task.setStatus(request.getStatus());
        task.setPriority(request.getPriority());
        task.setAssignee(userRepository.findById(request.getAssigneeId())
                .orElseThrow(() -> new EntityNotFoundException("Assignee not found" + request.getAssigneeId())));
        task = taskRepository.save(task);
        return TaskMapper.toResponse(task);
    }

    @Override
    public List<TaskResponse> read() {
        return taskRepository.findAll().stream()
                .map(TaskMapper::toResponse)
                .toList();
    }

    @Override
    public TaskResponse findById(Long id) {
        TaskEntity task = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task not found: " + id));
        return TaskMapper.toResponse(task);
    }

    @Override
    public TaskResponse updateStatus(Long id, String status) {
        TaskEntity task = taskRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Task not found: " + id));
        task.setStatus(status);
        task = taskRepository.save(task);
        return TaskMapper.toResponse(task);
    }

    @Override
    public void delete(Long itemId) {
        TaskEntity task = taskRepository.findById(itemId)
                .orElseThrow(() -> new EntityNotFoundException("Task not found: " + itemId));
        taskRepository.delete(task);
    }
}
