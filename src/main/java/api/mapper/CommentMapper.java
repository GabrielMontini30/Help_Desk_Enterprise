package api.mapper;

import api.dto.comment.CommentRequestDto;
import api.dto.comment.CommentResponseDto;
import api.entity.Comment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CommentMapper {
        Comment toComment(CommentRequestDto commentRequestDto);

        CommentResponseDto toResponseDto(Comment comment);
}
