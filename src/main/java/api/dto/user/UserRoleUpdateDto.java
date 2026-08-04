package api.dto.user;

import api.util.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record UserRoleUpdateDto(

        @Schema(description = "user role", example = "CLIENT")
        @NotNull(message = "the role not null")
        Role role) {
}
