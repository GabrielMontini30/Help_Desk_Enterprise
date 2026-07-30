package api.dto.ticket;

import api.util.Category;
import api.util.Priority;
import api.util.TicketStatus;
import jakarta.validation.constraints.NotNull;

public record TicketFilterDto(
        TicketStatus status,

        Priority priority,

        Category category,

        Long assignedTo
){
}
