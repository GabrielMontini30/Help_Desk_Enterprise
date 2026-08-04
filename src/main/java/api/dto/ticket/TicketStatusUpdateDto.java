package api.dto.ticket;

import api.util.TicketStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record TicketStatusUpdateDto(

        @Schema(description = "ticket status ", example ="CLOSED")
        @NotNull(message = "the status not null")
        TicketStatus status){
}