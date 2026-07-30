package api.controller;

import api.dto.comment.CommentRequestDto;
import api.dto.comment.CommentResponseDto;
import api.service.CommentService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/tickets/{ticketId}/comments")
    public ResponseEntity<CommentResponseDto> create(@RequestBody @Valid CommentRequestDto commentRequestDto, @PathVariable Long ticketId){
        return new ResponseEntity<>(commentService.create(commentRequestDto, ticketId), HttpStatus.CREATED);
    }

   @GetMapping("/tickets/{ticketId}/comments")
    public ResponseEntity<Page<CommentResponseDto>> listAllByTicket(@PathVariable  Long ticketId, Pageable pageable){
        return new ResponseEntity<>(commentService.listAllByTicket(ticketId, pageable), HttpStatus.OK);
    }

    @PutMapping ("/comments/{commentId}")
    public ResponseEntity<CommentResponseDto> update(@PathVariable  Long commentId, @RequestBody @Valid CommentRequestDto requestDto){
        return new ResponseEntity<>(commentService.update(commentId, requestDto), HttpStatus.OK);
    }

    @DeleteMapping("/comments/{commentId}")
    public ResponseEntity<Void> delete(@PathVariable  Long commentId){
        commentService.delete(commentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
