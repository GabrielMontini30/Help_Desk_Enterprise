package api.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record UserStatusUpdateDto(

        @Schema(description = "user is active", example = "true")
        @NotNull(message = "the active not null")
        Boolean active) {
}
