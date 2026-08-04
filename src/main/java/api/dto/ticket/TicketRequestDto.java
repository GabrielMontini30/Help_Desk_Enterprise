package api.dto.ticket;

import api.util.Category;
import api.util.Priority;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record TicketRequestDto(
        @Schema(description = "ticket tittle", example = "Unable to access the system")
        @NotBlank(message = "the title not blank")
        String title,

        @Schema(description = "Detailed problem description", example = "After login, the dashboard displays an internal server error.")
        @NotBlank(message = "the description not blank")
        String description,

        @Schema(description = "ticket priority ", example ="HIGH")
        Priority priority,

        @Schema(description = "ticket category ", example ="SOFTWARE")
        Category category
) {
}
