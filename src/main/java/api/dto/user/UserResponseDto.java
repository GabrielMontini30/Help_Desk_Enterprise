package api.dto.user;

import api.util.Role;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record UserResponseDto(
        @Schema(description = "user id", example = "1")
        Long id,

        @Schema(description = "user name", example = "Hector")
        String name,

        @Schema(description = "user email", example = "emailtest@gmail.com")
        String email,

        @Schema(description = "user role", example = "CLIENT")
        Role role,

        @Schema(description = "user department", example = "TI")
        String department,

        @Schema(description = "user is active", example = "true")
        Boolean active,

        @Schema(description = "user created at",  example = "2026-07-28T14:30:00")
        LocalDateTime createdAt
) {
}
