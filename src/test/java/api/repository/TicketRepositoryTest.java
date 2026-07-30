package api.repository;

import api.entity.Ticket;
import api.entity.User;
import api.factory.TicketFactory;
import api.factory.UserFactory;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

@DataJpaTest
public class TicketRepositoryTest {
    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private UserRepository userRepository;

    @Test
    @DisplayName("should return page by opened by users")
    void findByOpenedBy_ShouldReturnPage(){
        User user= UserFactory.createValidUser();
        user=userRepository.save(user);

        Ticket ticket= TicketFactory.createValidSavedTicket();
        ticket.setOpenedBy(user);
        ticketRepository.save(ticket);

        Pageable pageable = PageRequest.of(0, 10);

        Page<Ticket> result = ticketRepository.findByOpenedBy(user, pageable);

        Assertions.assertThat(result).isNotEmpty();
        Assertions.assertThat(result.get().toList()).hasSize(1);
        Assertions.assertThat(result.getContent().getFirst().getOpenedBy()).isEqualTo(user);

    }

    @Test
    @DisplayName("should return page by assigned user")
    void findByAssignedTo_ShouldReturnPage(){
        User user= UserFactory.createTechnician();
        user=userRepository.save(user);

        Ticket ticket= TicketFactory.createValidSavedTicket();
        ticket.setAssignedTo(user);
        ticket.setOpenedBy(user);

        ticketRepository.save(ticket);

        Pageable pageable = PageRequest.of(0, 10);

        Page<Ticket> result = ticketRepository.findByAssignedTo(user, pageable);

        Assertions.assertThat(result).isNotEmpty();
        Assertions.assertThat(result.get().toList()).hasSize(1);
        Assertions.assertThat(result.getContent().getFirst().getOpenedBy()).isEqualTo(user);
    }
}
