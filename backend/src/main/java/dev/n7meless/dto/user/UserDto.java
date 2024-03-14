package dev.n7meless.dto.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class UserDto {
    Long id;
    String email;
    @JsonProperty(value = "first_name")
    String firstName;
    @JsonProperty(value = "last_name")
    String lastName;
}
