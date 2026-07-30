package api.dto.ticket;

import jakarta.validation.constraints.NotNull;

public record TicketAssignDto(@NotNull(message = "the id not null") Long technicianId) {
}
