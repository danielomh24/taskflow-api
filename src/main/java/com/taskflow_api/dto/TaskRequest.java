package com.taskflow_api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TaskRequest(
        @NotBlank(message = "El título de la tarea es obligatorio")
        @Size(max = 100, message = "El título no puede exceder 100 caracteres")
        String title,

        String description,

        Boolean completed
) {
}
