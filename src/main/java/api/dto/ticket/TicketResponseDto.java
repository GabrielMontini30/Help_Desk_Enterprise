package api.dto.ticket;

import api.entity.User;
import api.util.Category;
import api.util.Priority;
import api.util.TicketStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record TicketResponseDto(
        @Schema(description = "ticket id", example = "1")
        Long id,

        @Schema(description = "ticket tittle", example = "Unable to access the system")
        String title,

        @Schema(description = "Detailed problem description", example = "After login, the dashboard displays an internal server error.")
        String description,

        @Schema(description = "ticket status ", example ="CLOSED")
        TicketStatus status,

        @Schema(description = "ticket priority ", example ="HIGH")
        Priority priority,

        @Schema(description = "ticket category ", example ="SOFTWARE")
        Category category,

        @Schema(description = "ticket assignedTo ", example ="emailtest@gmail.com")
        String assignedTo,

        @Schema(description = "ticket openedBy ", example ="emailtest@gmail.com")
        String openedBy,

        @Schema(description = "ticket created at", example = "2026-07-28T14:30:00")
        LocalDateTime createdAt,

        @Schema(description = "ticket updated at", example = "2026-07-28T14:30:00")
        LocalDateTime updatedAt,

        @Schema(description = "ticket sla deadline at", example = "2026-07-28T14:30:00")
        LocalDateTime slaDeadline
        )
{}
