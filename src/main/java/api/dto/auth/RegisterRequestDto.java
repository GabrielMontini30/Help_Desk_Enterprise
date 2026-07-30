package api.dto.auth;

import api.util.Role;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterRequestDto(
       @NotBlank( message = "the name not blank")
        String name,

        @Email
        String email,

        @NotBlank(message = "the password not blank")
        String password,

        @NotBlank(message = "the department not blank")
        String department,

        Role role
) {
}
