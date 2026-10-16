package api.dto.auth;

import api.util.Department;
import api.util.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RegisterRequestDto(
        @Schema(description = "user name", example = "Hector")
       @NotBlank( message = "the name not blank")
        String name,

        @Schema(description = "user email", example = "emailtest@gmail.com")
        @Email
        String email,

        @Schema(description = "user password", example = "test123@")
        @NotBlank(message = "the password not blank")
        @Size(min = 6, max = 30)
        @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&#]).+$")
        String password,

        @Schema(description = "user department", example = "IT")
        Department department,

        @Schema(description = "user role", example = "CLIENT")
        Role role
) {
}
