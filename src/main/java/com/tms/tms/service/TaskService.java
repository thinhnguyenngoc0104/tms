package com.tms.tms.service;

import java.util.List;

import com.tms.tms.io.TaskRequest;
import com.tms.tms.io.TaskResponse;

public interface TaskService {
    TaskResponse add(TaskRequest request);
    TaskResponse update(TaskRequest request, Long taskId);
    List<TaskResponse> read();
    TaskResponse findById(Long id);
    TaskResponse updateStatus(Long id, String status);
    void delete(Long itemId);
    Long getProjectIdByTaskId(Long taskId);
}
