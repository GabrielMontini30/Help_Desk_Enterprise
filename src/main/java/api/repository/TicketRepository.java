package api.repository;

import api.entity.Ticket;
import api.entity.User;

import api.util.TicketStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TicketRepository extends JpaRepository<Ticket, Long> {

    Page<Ticket> findAll(Pageable pageable);

    Page<Ticket> findByOpenedBy(User user, Pageable pageable);

    Page<Ticket> findByAssignedTo(User user,Pageable pageable);

}
