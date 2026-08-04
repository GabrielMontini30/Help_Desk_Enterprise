package api.dto.ticket;

import api.util.Category;
import api.util.Priority;
import api.util.TicketStatus;
import io.swagger.v3.oas.annotations.media.Schema;

public record TicketFilterDto(
        @Schema(description = "ticket status ", example ="CLOSED")
        TicketStatus status,

        @Schema(description = "ticket priority ", example ="HIGH")
        Priority priority,

        @Schema(description = "ticket category ", example ="SOFTWARE")
        Category category,

        @Schema(description = "ticket assignedTo ", example ="1")
        Long assignedTo
){
}
