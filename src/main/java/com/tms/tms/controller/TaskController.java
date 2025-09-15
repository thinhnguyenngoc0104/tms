package com.tms.tms.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.tms.tms.io.TaskRequest;
import com.tms.tms.io.TaskResponse;
import com.tms.tms.service.TaskService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/tasks")
public class TaskController {
    
    private final TaskService taskService;

    @GetMapping    
    public List<TaskResponse> fetchTasks() {
        return taskService.read();
    }

    @PostMapping    
    @ResponseStatus(HttpStatus.CREATED)
    public TaskResponse addTask(@RequestBody TaskRequest request) {
        return taskService.add(request);
    }

    @PutMapping("/{taskId}")    
    @ResponseStatus(HttpStatus.OK)
    public TaskResponse updateTask(@RequestBody TaskRequest request, @PathVariable Long taskId) {
        return taskService.update(request, taskId);
    }

    @DeleteMapping("/{taskId}")    
    @ResponseStatus(HttpStatus.OK)
    public void deleteTask(@PathVariable Long taskId) {
        taskService.delete(taskId);;        
    }
}
