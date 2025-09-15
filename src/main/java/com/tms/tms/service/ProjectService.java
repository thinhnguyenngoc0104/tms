package com.tms.tms.service;

import java.util.List;

import com.tms.tms.io.ProjectRequest;
import com.tms.tms.io.ProjectResponse;

public interface ProjectService {
    ProjectResponse add(ProjectRequest request);

    ProjectResponse update(ProjectRequest request, Long id);

    List<ProjectResponse> read();

    void delete(Long id);
}
