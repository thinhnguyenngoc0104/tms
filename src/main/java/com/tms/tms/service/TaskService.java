package com.tms.tms.service;

import com.tms.tms.io.TaskRequest;
import com.tms.tms.io.TaskResponse;
import com.tms.tms.io.TaskStatusUpdateRequest;

public interface TaskService {
    TaskResponse add(TaskRequest request);
    TaskResponse update(TaskRequest request, Long taskId);
    TaskResponse findById(Long id);
    TaskResponse updateStatus(Long id, TaskStatusUpdateRequest request);
    void delete(Long id);
}
