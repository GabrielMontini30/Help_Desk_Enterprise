package api.controller;

import api.dto.comment.CommentRequestDto;
import api.dto.comment.CommentResponseDto;
import api.service.CommentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Tag(name = "Comments", description = "Endpoint for managing support comments.")
public class CommentController {

    private final CommentService commentService;

    @PostMapping("/tickets/{ticketId}/comments")
    @Operation(summary = "Create comment", description = "Creates a new comment associated with a support ticket.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Comment created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid comment request"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "404", description = "Ticket not found"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    public ResponseEntity<CommentResponseDto> create(@RequestBody @Valid CommentRequestDto commentRequestDto, @PathVariable Long ticketId){
        return new ResponseEntity<>(commentService.create(commentRequestDto, ticketId), HttpStatus.CREATED);
    }

   @GetMapping("/tickets/{ticketId}/comments")
   @Operation(summary = "List ticket comments", description = "Returns a paginated list of comments for a ticket.")
   @ApiResponses({
           @ApiResponse(responseCode = "200", description = "Comments returned successfully"),
           @ApiResponse(responseCode = "401", description = "Authentication required"),
           @ApiResponse(responseCode = "403", description = "Access denied"),
           @ApiResponse(responseCode = "404", description = "Ticket not found"),
           @ApiResponse(responseCode = "500", description = "server error")
   })
    public ResponseEntity<Page<CommentResponseDto>> listAllByTicket(@PathVariable  Long ticketId, Pageable pageable){
        return new ResponseEntity<>(commentService.listAllByTicket(ticketId, pageable), HttpStatus.OK);
    }

    @PutMapping ("/comments/{commentId}")
    @Operation(summary = "Update comment", description = "Updates an existing comment.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Comment updated successfully "),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Comment not found"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    public ResponseEntity<CommentResponseDto> update(@PathVariable  Long commentId, @RequestBody @Valid CommentRequestDto requestDto){
        return new ResponseEntity<>(commentService.update(commentId, requestDto), HttpStatus.OK);
    }

    @DeleteMapping("/comments/{commentId}")
    @Operation(summary = "Delete comment", description = "Deletes an existing comment.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "No content delete a comment "),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Comment not found"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    public ResponseEntity<Void> delete(@PathVariable  Long commentId){
        commentService.delete(commentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
