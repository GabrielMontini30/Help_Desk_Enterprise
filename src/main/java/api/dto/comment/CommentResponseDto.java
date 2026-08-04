package api.dto.comment;

import api.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

public record CommentResponseDto (
        @Schema(description = "comment id", example = "1")
        Long id,

        @Schema(description = "comment message", example = "The internet is bad.")
        String message,

        @Schema(description = "comment author", example = "Hector")
        User author,

        @Schema(description = "comment internal", example = "true")
        Boolean internal,

        @Schema(description = "comment created at", example = "2026-07-28T14:30:00")
        LocalDateTime createdAt
){
}
