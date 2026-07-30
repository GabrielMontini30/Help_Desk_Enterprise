package api.service;

import api.dto.comment.CommentRequestDto;
import api.dto.comment.CommentResponseDto;
import api.entity.Comment;
import api.entity.Ticket;
import api.entity.User;
import api.exception.TicketClosedException;
import api.exception.UnauthorizedActionException;
import api.factory.CommentFactory;
import api.factory.DtoFactory;
import api.factory.TicketFactory;
import api.factory.UserFactory;
import api.mapper.CommentMapper;
import api.repository.CommentRepository;
import api.repository.TicketRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
public class CommentServiceTest {

    @Mock
    private  CommentRepository commentRepository;
    @Mock
    private  TicketRepository ticketRepository;
    @Mock
    private  CommentMapper commentMapper;

    @InjectMocks
    private CommentService commentService;

    @BeforeEach
    void setup(){

        MockitoAnnotations.openMocks(this);

        commentService= new CommentService(commentRepository,ticketRepository,commentMapper);
    }

    @Test
    @DisplayName("Should create comment when request is valid")
    void create_WhenRequestIsValid_ShouldCreateComment(){
        Ticket ticket= TicketFactory.createValidSavedTicket();

        User user= UserFactory.createValidUser();


        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities())
        );
        CommentRequestDto commentRequestDto= DtoFactory.createCommentRequest();
        CommentResponseDto commentResponseDto= new CommentResponseDto(1L,"Public comment", user,false,LocalDateTime.now());
        Comment comment= CommentFactory.createPublicComment();

        given(ticketRepository.findById(ticket.getId())).willReturn(Optional.of(ticket));
        given(commentRepository.save(comment)).willReturn(comment);
        given(commentMapper.toComment(commentRequestDto)).willReturn(comment);
        given(commentMapper.toResponseDto(comment)).willReturn(commentResponseDto);

        CommentResponseDto results = commentService.create(commentRequestDto, ticket.getId());

        Assertions.assertThat(results).isNotNull();
        Assertions.assertThat(results.author()).isEqualTo(user);
        Assertions.assertThat(results.message()).isEqualTo(comment.getMessage());
        Assertions.assertThat(results.id()).isEqualTo(comment.getId());

        then(ticketRepository).should().findById(ticket.getId());
        then(commentRepository).should().save(comment);
        then(commentMapper).should().toComment(commentRequestDto);
        then(commentMapper).should().toResponseDto(comment);

    }

    @Test
    @DisplayName("Should throw TicketClosedException when ticket is closed")
    void create_WhenTicketIsClosed_ShouldThrowTicketClosedException(){
        Ticket ticket= TicketFactory.createClosedTicket();
        CommentRequestDto commentRequestDto= DtoFactory.createCommentRequest();

        given(ticketRepository.findById(ticket.getId())).willReturn(Optional.of(ticket));

        Assertions.assertThatExceptionOfType(TicketClosedException.class).isThrownBy(()->commentService.create(commentRequestDto, ticket.getId()));

        then(ticketRepository).should().findById(ticket.getId());
    }

    @Test
    @DisplayName("Should return only public comments for client user")
    void listAllByTicket_ShouldReturnPageOfComments(){
        Ticket ticket= TicketFactory.createValidSavedTicket();
        User user= UserFactory.createValidUser();
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()));
        Comment comment= CommentFactory.createPublicComment();
        CommentResponseDto commentResponseDto= new CommentResponseDto(1L,"Public comment", user,false,LocalDateTime.now());

        Pageable pageable = PageRequest.of(0, 10);

        Page<Comment> commentPage = new PageImpl<>(List.of(comment), pageable, 1);

        given(ticketRepository.findById(ticket.getId())).willReturn(Optional.of(ticket));
        given(commentRepository.findByTicketIdAndInternalFalse(ticket.getId(),pageable)).willReturn(commentPage);
        given(commentMapper.toResponseDto(comment)).willReturn(commentResponseDto);

        Page<CommentResponseDto> results = commentService.listAllByTicket(ticket.getId(), pageable);

        Assertions.assertThat(results).isNotNull();

        Assertions.assertThat(results.getContent()).hasSize(1);

        Assertions.assertThat(results.getContent().getFirst()).isEqualTo(commentResponseDto);

        then(ticketRepository).should().findById(ticket.getId());
        then(commentRepository).should().findByTicketIdAndInternalFalse(ticket.getId(),pageable);
        then(commentMapper).should().toResponseDto(comment);


    }

    @Test
    @DisplayName("Should update comment when author edits it")
    void update_WhenAuthorEditsComment_ShouldUpdateComment(){
        Comment comment= CommentFactory.createValidComment();
        User user= UserFactory.createValidUser();
        CommentRequestDto commentRequestDto= DtoFactory.createCommentRequest();
        CommentResponseDto commentResponseDto= new CommentResponseDto(1L,"Public comment", user,false,comment.getCreatedAt());
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()));

        given(commentRepository.findById(comment.getId())).willReturn(Optional.of(comment));
        given(commentRepository.save(comment)).willReturn(comment);
        given(commentMapper.toResponseDto(comment)).willReturn(commentResponseDto);

        CommentResponseDto results = commentService.update(comment.getId(), commentRequestDto);

        Assertions.assertThat(results).isNotNull();
        Assertions.assertThat(results.message()).isEqualTo(commentRequestDto.message());
        Assertions.assertThat(comment.getMessage()).isEqualTo(commentRequestDto.message());
        Assertions.assertThat(comment.isInternal()).isEqualTo(commentRequestDto.internal());


        then(commentRepository).should().findById(comment.getId());
        then(commentRepository).should().save(comment);
        then(commentMapper).should().toResponseDto(comment);

    }

    @Test
    @DisplayName("Should throw UnauthorizedActionException when user is not the author or admin")
    void update_WhenUserIsNotAuthorAndNotAdmin_ShouldThrowUnauthorizedActionException(){

        Comment comment= CommentFactory.createValidCommentWithDifferentUserId();
        User user= UserFactory.createValidUser();
        CommentRequestDto commentRequestDto= DtoFactory.createCommentRequest();
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()));

        given(commentRepository.findById(comment.getId())).willReturn(Optional.of(comment));

        Assertions.assertThatExceptionOfType(UnauthorizedActionException.class).isThrownBy(()->commentService.update(comment.getId(),commentRequestDto));
    }

    @Test
    @DisplayName("Should delete comment when author deletes it")
    void delete_WhenAuthorDeletesComment_ShouldDeleteComment(){
        Comment comment= CommentFactory.createValidComment();
        User user= UserFactory.createValidUser();
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()));

        given(commentRepository.findById(comment.getId())).willReturn(Optional.of(comment));
        doNothing().when(commentRepository).delete(comment);

        Assertions.assertThatCode(()-> commentService.delete(comment.getId())).doesNotThrowAnyException();

        then(commentRepository).should().findById(comment.getId());
        then(commentRepository).should().delete(comment);
    }
}
