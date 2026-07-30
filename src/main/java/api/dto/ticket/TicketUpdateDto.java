package api.dto.ticket;

import api.util.Category;
import api.util.Priority;
import jakarta.validation.constraints.NotBlank;

public record TicketUpdateDto (

        @NotBlank(message = "the title not blank")
        String title,

        @NotBlank(message = "the description not blank")
        String description,

        Priority priority,

        Category category

){}
