package api.dto.ticket;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record TicketAssignDto(
        @Schema(description = "ticket id",example = "1")
        @NotNull(message = "the id not null")
        Long technicianId) {
}
