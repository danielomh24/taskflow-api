package com.taskflow_api.mapper;

import com.taskflow_api.dto.TaskResponse;
import com.taskflow_api.model.Task;

public class TaskMapper {
    public static TaskResponse toResponse(Task task) {
        return new TaskResponse(
                task.getId(),
                task.getTitle(),
                task.getDescription(),
                task.isCompleted(),
                task.getCreatedAt(),
                task.getUpdatedAt()
        );
    }
}
