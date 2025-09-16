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

import com.tms.tms.common.util.AuthUtil;
import com.tms.tms.io.DeleteResponse;
import com.tms.tms.io.ProjectRequest;
import com.tms.tms.io.ProjectResponse;
import com.tms.tms.io.TaskResponse;
import com.tms.tms.service.ProjectService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;
    private final AuthUtil authUtil;

    @GetMapping
    public List<ProjectResponse> fetchProjects() {
        return projectService.read();
    }

    @GetMapping("/{id}")
    public ProjectResponse getProjectById(@PathVariable Long id) {
        return projectService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProjectResponse addProject(@RequestBody ProjectRequest request) {
        // Set the current authenticated user as the owner
        if (request.getOwnerId() == null) {
            request.setOwnerId(authUtil.getCurrentUserId());
        }
        return projectService.add(request);
    }

    @PutMapping("/{id}")
    public ProjectResponse updateProject(@PathVariable Long id, @RequestBody ProjectRequest request) {
        return projectService.update(request, id);
    }

    @DeleteMapping("/{id}")
    public DeleteResponse deleteProject(@PathVariable Long id) {
        projectService.delete(id);
        return DeleteResponse.builder()
                .message("Project deleted successfully")
                .deletedId(id)
                .build();
    }

    @GetMapping("/{projectId}/tasks")
    public List<TaskResponse> getProjectTasks(@PathVariable Long projectId) {
        return projectService.getProjectTasks(projectId);
    }
}
