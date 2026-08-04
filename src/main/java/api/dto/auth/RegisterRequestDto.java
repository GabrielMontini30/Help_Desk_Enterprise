package api.dto.auth;

import api.util.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegisterRequestDto(
        @Schema(description = "user name", example = "Hector")
       @NotBlank( message = "the name not blank")
        String name,

        @Schema(description = "user email", example = "emailtest@gmail.com")
        @Email
        String email,

        @Schema(description = "user password", example = "test123")
        @NotBlank(message = "the password not blank")
        String password,

        @Schema(description = "user department", example = "TI")
        @NotBlank(message = "the department not blank")
        String department,

        @Schema(description = "user role", example = "CLIENT")
        Role role
) {
}
