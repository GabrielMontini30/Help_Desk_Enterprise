package api.service;

import api.dto.comment.CommentRequestDto;
import api.dto.comment.CommentResponseDto;
import api.entity.Comment;
import api.entity.Ticket;
import api.entity.User;
import api.exception.ResourceNotFoundException;
import api.exception.TicketClosedException;
import api.exception.UnauthorizedActionException;
import api.mapper.CommentMapper;
import api.repository.CommentRepository;
import api.repository.TicketRepository;
import api.util.Role;
import api.util.TicketStatus;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;



@Service
@AllArgsConstructor

public class CommentService {
    private final CommentRepository commentRepository;
    private final TicketRepository ticketRepository;
    private final CommentMapper commentMapper;

    public CommentResponseDto create(CommentRequestDto commentRequest, Long ticketId){

        Ticket ticket = ticketRepository.findById(ticketId).orElseThrow(() -> new ResourceNotFoundException("The Ticket not founded."));

        if(ticket.getStatus()== TicketStatus.CLOSED){
            throw new TicketClosedException("the ticket not modify if it is closed");
        }

        User user = getLoggedUser();

        Comment commentToSaved = commentMapper.toComment(commentRequest);

        commentToSaved.setAuthor(user);
        commentToSaved.setTicket(ticket);

        commentRepository.save(commentToSaved);

        return commentMapper.toResponseDto(commentToSaved);
    }


    public Page<CommentResponseDto> listAllByTicket(Long ticketId, Pageable pageable) {

        ticketRepository.findById(ticketId).orElseThrow(() -> new ResourceNotFoundException("Ticket not found."));

        User loggedUser = getLoggedUser();

        Page<Comment> comments;

        if(loggedUser.getRole()== Role.CLIENT) {
            comments = commentRepository.findByTicketIdAndInternalFalse(ticketId, pageable);
        } else {
            comments = commentRepository.findByTicketId(ticketId, pageable);
        }

        return comments.map(commentMapper::toResponseDto);
    }

    public CommentResponseDto update(Long idComment, CommentRequestDto requestDto){

        Comment commentToUpdate = commentRepository.findById(idComment).orElseThrow(() -> new ResourceNotFoundException("the comment not found"));

        if(commentToUpdate.getTicket().getStatus()== TicketStatus.CLOSED){
            throw new TicketClosedException("the ticket is closed.");
        }

        if(!getLoggedUser().getId().equals(commentToUpdate.getAuthor().getId()) && getLoggedUser().getRole()!=Role.ADMIN)  {
            throw new UnauthorizedActionException("You are not authorization to edit a comment");
        }

        commentToUpdate.setMessage(requestDto.message());
        commentToUpdate.setInternal(requestDto.internal());

        commentRepository.save(commentToUpdate);

        return commentMapper.toResponseDto(commentToUpdate);
    }

    public void delete(Long idComment){
        Comment commentToDelete = commentRepository.findById(idComment).orElseThrow(() -> new ResourceNotFoundException("the comment not found"));

        if(commentToDelete.getTicket().getStatus()== TicketStatus.CLOSED){
            throw new TicketClosedException("the ticket is closed.");
        }

        if(!getLoggedUser().getId().equals(commentToDelete.getAuthor().getId()) && getLoggedUser().getRole()!=Role.ADMIN){
            throw new UnauthorizedActionException("You are not authorization to edit a comment");
        }

        commentRepository.delete(commentToDelete);
    }

    private User getLoggedUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }
}
