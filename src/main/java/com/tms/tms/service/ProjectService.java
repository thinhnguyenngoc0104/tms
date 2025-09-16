package com.tms.tms.service;

import java.util.List;

import com.tms.tms.io.ProjectRequest;
import com.tms.tms.io.ProjectResponse;
import com.tms.tms.io.TaskResponse;

public interface ProjectService {
    ProjectResponse add(ProjectRequest request);
    ProjectResponse update(ProjectRequest request, Long id);
    List<ProjectResponse> read();
    ProjectResponse findById(Long id);
    void delete(Long id);
    List<TaskResponse> getProjectTasks(Long projectId);
}
