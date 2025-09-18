package com.tms.tms.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.tms.tms.io.DeleteResponse;
import com.tms.tms.io.TaskRequest;
import com.tms.tms.io.TaskResponse;
import com.tms.tms.io.TaskStatusUpdateRequest;
import com.tms.tms.service.AuthorizationService;
import com.tms.tms.service.TaskService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class TaskController {

    private final TaskService taskService;
    private final AuthorizationService authorizationService;

    @GetMapping("/tasks/{id}")
    public TaskResponse getTaskById(@PathVariable Long id) {
        // Check if user can access the project that contains this task
        Long projectId = taskService.getProjectIdByTaskId(id);
        authorizationService.requireProjectAccess(projectId);
        return taskService.findById(id);
    }

    @PostMapping("/tasks")
    @ResponseStatus(HttpStatus.CREATED)
    public TaskResponse addTask(@RequestBody TaskRequest request) {
        // Check if user can modify tasks in the specified project
        authorizationService.requireProjectAccess(request.getProjectId());
        return taskService.add(request);
    }

    @PutMapping("/tasks/{id}")
    public TaskResponse updateTask(@PathVariable Long id, @RequestBody TaskRequest request) {
        // Check if user can modify tasks in the project that contains this task
        Long projectId = taskService.getProjectIdByTaskId(id);
        authorizationService.requireProjectAccess(projectId);
        return taskService.update(request, id);
    }

    @PatchMapping("/tasks/{id}/status")
    public TaskResponse updateTaskStatus(@PathVariable Long id, @RequestBody TaskStatusUpdateRequest request) {
        // Check if user can modify tasks in the project that contains this task
        Long projectId = taskService.getProjectIdByTaskId(id);
        authorizationService.requireProjectAccess(projectId);
        return taskService.updateStatus(id, request.getStatus());
    }

    @DeleteMapping("/tasks/{id}")
    public DeleteResponse deleteTask(@PathVariable Long id) {
        // Check if user can modify tasks in the project that contains this task
        Long projectId = taskService.getProjectIdByTaskId(id);
        authorizationService.requireProjectAccess(projectId);
        taskService.delete(id);
        return DeleteResponse.builder()
                .message("Task deleted successfully")
                .deletedId(id)
                .build();
    }
}
