package com.tms.tms.service;

import java.util.List;

import com.tms.tms.io.TaskRequest;
import com.tms.tms.io.TaskResponse;

public interface TaskService {
    TaskResponse add(TaskRequest request);

    TaskResponse update(TaskRequest request, Long taskId);

    List<TaskResponse> read();

    void delete(Long itemId);
}
