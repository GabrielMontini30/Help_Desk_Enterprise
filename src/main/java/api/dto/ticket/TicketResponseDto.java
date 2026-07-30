package api.dto.ticket;

import api.entity.User;
import api.util.Category;
import api.util.Priority;
import api.util.TicketStatus;

import java.time.LocalDateTime;

public record TicketResponseDto(
        Long id,
        String title,
        String description,
        TicketStatus status,
        Priority priority,
        Category category,
        String assignedTo,
        String openedBy,
        LocalDateTime createdAt,
        LocalDateTime updatedAt,
        LocalDateTime slaDeadline
        )
{}
