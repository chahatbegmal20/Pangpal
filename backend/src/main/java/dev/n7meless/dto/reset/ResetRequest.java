package dev.n7meless.dto.reset;

import com.fasterxml.jackson.annotation.JsonProperty;

public record ResetRequest(String password,
                           @JsonProperty("password_confirm") String passwordConfirm,
                           String token) {
}
