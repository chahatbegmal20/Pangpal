package dev.n7meless.dto.register;

import com.fasterxml.jackson.annotation.JsonProperty;

public record RegisterRequest(String email, @JsonProperty("first_name") String firstName,
                              @JsonProperty("last_name") String lastName, String password,
                              @JsonProperty("password_confirm") String passwordConfirm) {
}