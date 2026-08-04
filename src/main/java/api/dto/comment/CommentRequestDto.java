package api.dto.comment;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record CommentRequestDto (
        @Schema(description = "comment message", example = "The internet is bad.")
        @NotBlank(message = "the message not blank")
        String message,

        @Schema(description = "comment internal", example = "true")
        Boolean internal
){
}
