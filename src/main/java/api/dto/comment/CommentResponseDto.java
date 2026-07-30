package api.dto.comment;

import api.entity.User;

import java.time.LocalDateTime;

public record CommentResponseDto (
        Long id,
        String message,
        User author,
        Boolean internal,
        LocalDateTime createdAt
){
}
