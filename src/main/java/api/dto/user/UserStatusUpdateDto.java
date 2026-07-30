package api.dto.user;

import jakarta.validation.constraints.NotNull;

public record UserStatusUpdateDto(@NotNull(message = "the active not null") Boolean active) {
}
