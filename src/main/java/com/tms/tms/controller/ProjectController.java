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
import com.tms.tms.io.ProjectMemberRequest;
import com.tms.tms.io.ProjectMemberResponse;
import com.tms.tms.io.TaskResponse;
import com.tms.tms.service.AuthorizationService;
import com.tms.tms.service.ProjectService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;
    private final AuthUtil authUtil;
    private final AuthorizationService authorizationService;

    @GetMapping
    public List<ProjectResponse> fetchProjects() {
        // Admin can see all projects, users see only projects they're members of
        if (authorizationService.isAdmin()) {
            return projectService.read();
        } else {
            // Filter projects for regular users - this will be implemented in service
            return projectService.read().stream()
                    .filter(project -> authorizationService.canAccessProject(project.getId()))
                    .toList();
        }
    }

    @GetMapping("/{id}")
    public ProjectResponse getProjectById(@PathVariable Long id) {
        // Check if user can access this project
        authorizationService.requireProjectAccess(id);
        return projectService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ProjectResponse addProject(@RequestBody ProjectRequest request) {
        // Only admin can create projects
        authorizationService.requireProjectPermission();

        // Set the current authenticated user as the owner of the project
        return projectService.add(request, authUtil.getCurrentUserId());
    }

    @PutMapping("/{id}")
    public ProjectResponse updateProject(@PathVariable Long id, @RequestBody ProjectRequest request) {
        // Only admin can update projects
        authorizationService.requireProjectPermission();
        return projectService.update(request, id);
    }

    @DeleteMapping("/{id}")
    public DeleteResponse deleteProject(@PathVariable Long id) {
        // Only admin can delete projects
        authorizationService.requireProjectPermission();
        projectService.delete(id);
        return DeleteResponse.builder()
                .message("Project deleted successfully")
                .deletedId(id)
                .build();
    }

    @GetMapping("/{projectId}/tasks")
    public List<TaskResponse> getProjectTasks(@PathVariable Long projectId) {
        // Check if user can access this project's tasks
        authorizationService.requireProjectAccess(projectId);
        return projectService.getProjectTasks(projectId);
    }

    // Member management endpoints
    @GetMapping("/{projectId}/members")
    public List<ProjectMemberResponse> getProjectMembers(@PathVariable Long projectId) {
        // Check if user can access this project
        authorizationService.requireProjectAccess(projectId);
        return projectService.getProjectMembers(projectId);
    }

    @PostMapping("/{projectId}/members")
    @ResponseStatus(HttpStatus.CREATED)
    public void addMemberToProject(@PathVariable Long projectId, @RequestBody ProjectMemberRequest request) {
        // Only admin can modify project members
        authorizationService.requireProjectPermission();
        projectService.addMemberToProject(projectId, request.getUserId());
    }

    @DeleteMapping("/{projectId}/members/{userId}")
    public void removeMemberFromProject(@PathVariable Long projectId, @PathVariable Long userId) {
        // Only admin can modify project members
        authorizationService.requireProjectPermission();
        projectService.removeMemberFromProject(projectId, userId);
    }
}
