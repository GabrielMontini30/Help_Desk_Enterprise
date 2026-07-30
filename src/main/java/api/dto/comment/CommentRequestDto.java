package api.dto.comment;

import jakarta.validation.constraints.NotBlank;

public record CommentRequestDto (
        @NotBlank(message = "the message not blank")
        String message,

        Boolean internal
){
}
