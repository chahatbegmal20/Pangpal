package dev.n7meless.dto.login;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record LoginRequest(@NotNull(message = "Email can not be null")
                           @NotBlank(message = "Email can not be empty")
                           String email,
                           @NotNull(message = "Password can not be null")
                           @NotBlank(message = "Password can not be empty")
                           String password) {
}
