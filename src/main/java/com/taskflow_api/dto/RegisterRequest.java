package com.taskflow_api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest (
    @NotBlank(message = "El nombre de usuario no puede estar vacío")
    @Size(min = 3, max = 20, message = "El usuario debe tener entre 3 y 20 caracteres")
    String username,

    @NotBlank(message = "El email no puede estar vacío")
    @Email(message = "El formato de email no es válido")
    String email,

    @NotBlank(message = "La contraseña no puede estar vacía")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    String password
){}
