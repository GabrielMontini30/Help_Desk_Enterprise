package api.dto.user;

import api.util.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserRoleUpdateDto(@NotNull(message = "the role not null") Role role) {
}
