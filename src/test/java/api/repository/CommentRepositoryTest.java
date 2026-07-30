package api.repository;

import api.entity.Comment;
import api.entity.Ticket;
import api.entity.User;
import api.factory.CommentFactory;
import api.factory.TicketFactory;
import api.factory.UserFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

@DataJpaTest
public class CommentRepositoryTest {

    @Autowired
    private  CommentRepository commentRepository;

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("Should return comments by ticket id")
    void findByTicketId_ShouldReturnComments(){

        User user = UserFactory.createValidUser();
        user = userRepository.save(user);

        Ticket ticket = TicketFactory.createValidSavedTicket();
        ticket.setOpenedBy(user);

        ticket = ticketRepository.save(ticket);

        Comment comment = CommentFactory.createValidComment();
        comment.setAuthor(user);
        comment.setTicket(ticket);

        commentRepository.save(comment);

        Pageable pageable = PageRequest.of(0,10);

        Page<Comment> result = commentRepository.findByTicketId(ticket.getId(), pageable);

        Assertions.assertThat(result).isNotEmpty();
        Assertions.assertThat(result.getContent()).hasSize(1);
        Assertions.assertThat(result.getContent().getFirst().getMessage()).isEqualTo(comment.getMessage());

        Assertions.assertThat(result.getContent().getFirst().getTicket()).isEqualTo(ticket);

    }

    @Test
    @DisplayName("Should return only public comments from ticket")
    void findByTicketIdAndInternalFalse_ShouldReturnOnlyPublicComments(){

        User user = UserFactory.createValidUser();
        user = userRepository.save(user);

        Ticket ticket = TicketFactory.createValidSavedTicket();
        ticket.setOpenedBy(user);

        ticket = ticketRepository.save(ticket);

        Comment commentInternal = CommentFactory.createInternalComment();
        commentInternal.setAuthor(user);
        commentInternal.setTicket(ticket);

        Comment commentPublic = CommentFactory.createPublicComment();
        commentPublic.setAuthor(user);
        commentPublic.setTicket(ticket);

        commentRepository.save(commentInternal);
        commentRepository.save(commentPublic);

        Pageable pageable = PageRequest.of(0,10);

        Page<Comment> result = commentRepository.findByTicketIdAndInternalFalse(ticket.getId(), pageable);


        Assertions.assertThat(result).isNotEmpty();
        Assertions.assertThat(result.getContent()).hasSize(1);
        Assertions.assertThat(result.getContent().getFirst().isInternal()).isFalse();
        Assertions.assertThat(result.getContent().getFirst().getId()).isEqualTo(commentPublic.getId());
    }
}
