package api.mapper;

import api.dto.comment.CommentRequestDto;
import api.dto.comment.CommentResponseDto;
import api.entity.Comment;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CommentMapper {
        Comment toComment(CommentRequestDto commentRequestDto);

        CommentResponseDto toResponseDto(Comment comment);
}
