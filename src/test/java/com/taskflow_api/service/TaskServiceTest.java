package com.taskflow_api.service;

import com.taskflow_api.dto.TaskRequest;
import com.taskflow_api.dto.TaskResponse;
import com.taskflow_api.model.Task;
import com.taskflow_api.model.User;
import com.taskflow_api.repository.TaskRepository;
import com.taskflow_api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@DisplayName("Pruebas para TaskService")
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TaskService taskService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("Debería crear una tarea exitosamente")
    @Test
    public void testCreateTaskSuccess() {
        // Arrange
        TaskRequest request = new TaskRequest("Tarea 1", "Descripción de la tarea", true);
        User user = User.builder()
                .id(1L)
                .username("usuario1")
                .password(passwordEncoder.encode("password"))
                .build();

        when(userRepository.findByUsername(eq("usuario1"))).thenReturn(Optional.of(user));

        Task taskToSave = Task.builder()
                .title(request.title())
                .description(request.description())
                .completed(false)
                .user(user)
                .build();

        Task savedTask = Task.builder()
                        .id(1L)
                .title("Tarea 1")
                .description("Descripción de la tarea")
                .completed(false)
                .user(user)
                .build();

        when(taskRepository.save(any(Task.class))).thenReturn(savedTask);

        // Act
        TaskResponse response = taskService.createTask(request, "usuario1");

        // Assert
        assertNotNull(response);
        assertEquals("Tarea 1", response.title());
        verify(userRepository, times(1)).findByUsername(eq("usuario1"));
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @DisplayName("Debería obtener una lista de tareas exitosamente")
    @Test
    public void testGetTasksSuccess() {
        // Arrange
        User user = User.builder()
                .id(1L)
                .username("usuario1")
                .password(passwordEncoder.encode("password"))
                .build();

        when(userRepository.findByUsername(eq("usuario1"))).thenReturn(Optional.of(user));

        Task task1 = Task.builder()
                .id(1L)
                .title("Tarea 1")
                .description("Descripción de la tarea")
                .completed(false)
                .user(user)
                .build();
        Task task2 = Task.builder()
                .id(1L)
                .title("Tarea 2")
                .description("Otra descripción de la tarea")
                .completed(true)
                .user(user)
                .build();

        when(taskRepository.findByUserId(eq(user.getId()))).thenReturn(List.of(task1, task2));

        // Act
        List<TaskResponse> responses = taskService.getTasks("usuario1", null);

        // Assert
        assertNotNull(responses);
        assertEquals(2, responses.size());
        verify(userRepository, times(1)).findByUsername(eq("usuario1"));
        verify(taskRepository, times(1)).findByUserId(eq(user.getId()));
    }

    @DisplayName("Debería obtener una tarea por ID exitosamente")
    @Test
    public void testGetTaskByIdSuccess() {
        // Arrange
        User user = new User();
        user.setId(1l);
        user.setUsername("usuario1");
        user.setPassword("password");
        when(userRepository.findByUsername(eq("usuario1"))).thenReturn(Optional.of(user));

        Task task = Task.builder()
                .id(1L)
                .title("Tarea 1")
                .description("Descripción de la tarea")
                .completed(false)
                .user(user)
                .build();

        when(taskRepository.findByIdAndUserId(eq(1L), eq(user.getId()))).thenReturn(Optional.of(task));

        // Act
        TaskResponse response = taskService.getTaskById(1L, "usuario1");

        // Assert
        assertNotNull(response);
        assertEquals("Tarea 1", response.title());
        verify(userRepository, times(1)).findByUsername(eq("usuario1"));
        verify(taskRepository, times(1)).findByIdAndUserId(eq(1L), eq(user.getId()));
    }

    @DisplayName("Debería actualizar una tarea exitosamente")
    @Test
    public void testUpdateTaskSuccess() {
        // Arrange
        User user = User.builder()
                .id(1L)
                .username("usuario1")
                .password(passwordEncoder.encode("password"))
                .build();
        when(userRepository.findByUsername(eq("usuario1"))).thenReturn(Optional.of(user));

        Task task = Task.builder()
                .id(1L)
                .title("Tarea 1")
                .description("Descripción de la tarea")
                .completed(false)
                .user(user)
                .build();
        when(taskRepository.findByIdAndUserId(eq(1L), eq(user.getId()))).thenReturn(Optional.of(task));

        TaskRequest request = new TaskRequest("Nueva tarea", "Nueva Descripcion", false);

        task.setTitle(request.title());
        task.setDescription(request.description());

        Task updatedTask = Task.builder()
                        .id(1L)
                .title("Nueva tarea")
                .description("Nueva Descripcion")
                .completed(false)
                .user(user)
                .build();
        when(taskRepository.save(any(Task.class))).thenReturn(updatedTask);

        // Act
        TaskResponse response = taskService.updateTask(1L, request, "usuario1");

        // Assert
        assertNotNull(response);
        assertEquals("Nueva tarea", response.title());
        verify(userRepository, times(1)).findByUsername(eq("usuario1"));
        verify(taskRepository, times(1)).findByIdAndUserId(eq(1L), eq(user.getId()));
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @DisplayName("Debería cambiar el estado de una tarea exitosamente")
    @Test
    public void testToggleTaskCompletionSuccess() {
        // Arrange
        User user = User.builder()
                .id(1L)
                .username("usuario1")
                .password(passwordEncoder.encode("password"))
                .build();
        when(userRepository.findByUsername(eq("usuario1"))).thenReturn(Optional.of(user));

        Task task = Task.builder()
                .id(1L)
                .title("Tarea 1")
                .description("Descripción de la tarea")
                .completed(false)
                .user(user)
                .build();
        when(taskRepository.findByIdAndUserId(eq(1L), eq(user.getId()))).thenReturn(Optional.of(task));

        Task updatedTask = Task.builder()
                .id(1L)
                .title("Tarea 1")
                .description("Descripción de la tarea")
                .completed(true)
                .user(user)
                .build();
        when(taskRepository.save(any(Task.class))).thenReturn(updatedTask);

        // Act
        TaskResponse response = taskService.toggleTaskCompletion(1L, "usuario1");

        // Assert
        assertNotNull(response);
        assertTrue(response.completed());
        verify(userRepository, times(1)).findByUsername(eq("usuario1"));
        verify(taskRepository, times(1)).findByIdAndUserId(eq(1L), eq(user.getId()));
        verify(taskRepository, times(1)).save(any(Task.class));
    }

    @DisplayName("Debería eliminar una tarea exitosamente")
    @Test
    public void testDeleteTaskSuccess() {
        // Arrange
        User user = User.builder()
                .id(1L)
                .username("usuario1")
                .password(passwordEncoder.encode("password"))
                .build();
        when(userRepository.findByUsername(eq("usuario1"))).thenReturn(Optional.of(user));

        Task task = Task.builder()
                .id(1L)
                .title("Tarea 1")
                .description("Descripción de la tarea")
                .completed(false)
                .user(user)
                .build();
        when(taskRepository.findByIdAndUserId(eq(1L), eq(user.getId()))).thenReturn(Optional.of(task));

        // Act
        taskService.deleteTask(1L, "usuario1");

        // Assert
        verify(userRepository, times(1)).findByUsername(eq("usuario1"));
        verify(taskRepository, times(1)).findByIdAndUserId(eq(1L), eq(user.getId()));
        verify(taskRepository, times(1)).delete(eq(task));
    }

    @DisplayName("Debería lanzar una excepción si el usuario no existe")
    @Test
    public void testGetTaskByIdUserNotFoundException() {
        // Arrange
        when(userRepository.findByUsername(eq("usuario1"))).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> taskService.getTaskById(1L, "usuario1"));
        verify(userRepository, times(1)).findByUsername(eq("usuario1"));
    }

    @DisplayName("Debería lanzar una excepción si la tarea no existe")
    @Test
    public void testGetTaskByIdTaskNotFoundException() {
        // Arrange
        User user = User.builder()
                .id(1L)
                .username("usuario1")
                .password(passwordEncoder.encode("password"))
                .build();
        when(userRepository.findByUsername(eq("usuario1"))).thenReturn(Optional.of(user));
        when(taskRepository.findByIdAndUserId(eq(1L), eq(user.getId()))).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> taskService.getTaskById(1L, "usuario1"));
        verify(userRepository, times(1)).findByUsername(eq("usuario1"));
        verify(taskRepository, times(1)).findByIdAndUserId(eq(1L), eq(user.getId()));
    }
}

