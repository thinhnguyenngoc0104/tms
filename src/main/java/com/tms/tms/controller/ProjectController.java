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

import com.tms.tms.io.ProjectRequest;
import com.tms.tms.io.ProjectResponse;
import com.tms.tms.service.ProjectService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/projects")
public class ProjectController {

    private final ProjectService projectService;

    @GetMapping    
    public List<ProjectResponse> fetchProjects() {
        return projectService.read();
    }

    @PostMapping    
    @ResponseStatus(HttpStatus.CREATED)
    public ProjectResponse addProject(@RequestBody ProjectRequest request) {
        return projectService.add(request);
    }

    @PutMapping("/{projectId}")    
    @ResponseStatus(HttpStatus.OK)
    public ProjectResponse updateProject(@RequestBody ProjectRequest request, @PathVariable String projectId) {
        return projectService.update(request, projectId);
    }

    @DeleteMapping("/{projectId}")    
    @ResponseStatus(HttpStatus.OK)
    public void deleteProject(@PathVariable String projectId) {
        projectService.delete(projectId);
    }
}
