package com.taskflow_api.service;

import com.taskflow_api.dto.TaskRequest;
import com.taskflow_api.dto.TaskResponse;
import com.taskflow_api.mapper.TaskMapper;
import com.taskflow_api.model.Task;
import com.taskflow_api.model.User;
import com.taskflow_api.repository.TaskRepository;
import com.taskflow_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TaskService {

    private final TaskRepository taskRepository;
    private final UserRepository userRepository;

    private User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado: " + username));
    }

    private Task getTaskByIdAndUser(long taskId, long userId) {
        return taskRepository.findByIdAndUserId(taskId, userId)
                .orElseThrow(() -> new IllegalArgumentException("Tarea no encontrada o no tiene permisos para acceder a ella. Id: " + taskId));
    }

    @Transactional
    public TaskResponse createTask(TaskRequest request, String username) {
        User user = getUserByUsername(username);

        Task task = Task.builder()
                .title(request.title())
                .description(request.description())
                .completed(request.completed() != null && request.completed())
                .user(user)
                .build();

        Task savedTask = taskRepository.save(task);
        return TaskMapper.toResponse(savedTask);
    }

    @Transactional(readOnly = true)
    public List<TaskResponse> getTasks(String username, Boolean completed) {
        User user = getUserByUsername(username);
        List<Task> tasks;

        if (completed == null) {
            tasks = taskRepository.findByUserId(user.getId());
        } else {
            tasks = taskRepository.findByUserIdAndCompleted(user.getId(), completed);
        }

        return tasks.stream()
                .map(TaskMapper::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public TaskResponse getTaskById(Long taskId, String username) {
        User user = getUserByUsername(username);
        Task task = getTaskByIdAndUser(taskId, user.getId());
        return TaskMapper.toResponse(task);
    }

    @Transactional
    public TaskResponse updateTask(Long taskId, TaskRequest request, String username) {
        User user = getUserByUsername(username);
        Task task = getTaskByIdAndUser(taskId, user.getId());

        task.setTitle(request.title());
        task.setDescription(request.description());
        if (request.completed() != null) {
            task.setCompleted(request.completed());
        }

        Task updatedTask = taskRepository.save(task);
        return TaskMapper.toResponse(updatedTask);
    }

    @Transactional
    public TaskResponse toggleTaskCompletion(Long taskId, String username) {
        User user = getUserByUsername(username);
        Task task = getTaskByIdAndUser(taskId, user.getId());
        task.setCompleted(!task.isCompleted());
        Task updateTask = taskRepository.save(task);
        return TaskMapper.toResponse(updateTask);
    }

    @Transactional
    public void deleteTask(Long taskId, String username) {
        User user = getUserByUsername(username);
        Task task = getTaskByIdAndUser(taskId, user.getId());
        taskRepository.delete(task);
    }
}
