package api.dto.ticket;

import api.util.TicketStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record TicketStatusUpdateDto(@NotNull(message = "the status not null") TicketStatus status){
}