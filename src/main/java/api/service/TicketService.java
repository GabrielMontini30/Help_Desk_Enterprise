package api.service;

import api.dto.ticket.TicketRequestDto;
import api.dto.ticket.TicketResponseDto;
import api.dto.ticket.TicketStatusUpdateDto;
import api.dto.ticket.TicketUpdateDto;
import api.entity.Ticket;
import api.entity.User;
import api.exception.*;
import api.mapper.TicketMapper;
import api.repository.TicketRepository;
import api.repository.UserRepository;
import api.util.Priority;
import api.util.Role;
import api.util.TicketStatus;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


@Service
@RequiredArgsConstructor
public class TicketService {
    private final TicketMapper ticketMapper;
    private final TicketRepository repository;
    private final UserRepository userRepository;

    @Transactional
    public TicketResponseDto createTicket(TicketRequestDto dto){

        Ticket ticket = ticketMapper.requestToEntity(dto);

        User userLogged = getLoggedUser();

        if(ticket.getPriority()==null) throw new BadRequestException("The priority not be null");

        ticket.setSlaDeadline(calculateDeadline(ticket.getPriority()));
        ticket.setStatus(TicketStatus.OPEN);
        ticket.setOpenedBy(userLogged);
        ticket.setAssignedTo(null);

        repository.save(ticket);

        return ticketMapper.entityToResponse(ticket);
    }

    public TicketResponseDto findById(Long id){
        Ticket ticket = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Id not found"));

        return ticketMapper.entityToResponse(ticket);
    }

    public Page<TicketResponseDto> listAll(Pageable pageable){
        Page<Ticket> allTicket = repository.findAll(pageable);

        return allTicket.map(ticketMapper::entityToResponse);
    }

    @Transactional
    public TicketResponseDto updatedTicket(Long id,TicketUpdateDto dto){
        Ticket ticketToUpdate = repository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Not found ticket"));

        if(dto==null) throw new BadRequestException("the ticket to update not be null");

        validateTicketNotClosed(ticketToUpdate);

        ticketToUpdate.setTitle(dto.title());
        ticketToUpdate.setCategory(dto.category());
        ticketToUpdate.setDescription(dto.description());
        ticketToUpdate.setPriority(dto.priority());


        Ticket ticketSave = repository.save(ticketToUpdate);

        return ticketMapper.entityToResponse(ticketSave);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    public void closeTicket(Long id){
        Ticket ticketToDelete = repository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Not found ticket"));

        ticketToDelete.setStatus(TicketStatus.CLOSED);
        
        ticketToDelete.setClosedAt(LocalDateTime.now());

        repository.save(ticketToDelete);
    }

    @PreAuthorize("hasRole('TECHNICIAN')")
    @Transactional
    public void changeStatus(Long id, TicketStatusUpdateDto statusUpdate){
        Ticket ticketToChange = repository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Not found ticket"));

        if(ticketToChange.getStatus()==TicketStatus.CLOSED){
            throw new TicketClosedException("not change if ticket status is 'closed'. ");
        }

        if(statusUpdate.status()==TicketStatus.CLOSED) {
           closeTicket(ticketToChange.getId());
        }else{
            ticketToChange.setStatus(statusUpdate.status());
            repository.save(ticketToChange);
        }
    }

    @PreAuthorize("hasRole('TECHNICIAN')")
    @Transactional
    public TicketResponseDto assignTicket(Long ticketId, Long technicianId){

        Ticket ticket = repository.findById(ticketId).orElseThrow(()-> new ResourceNotFoundException("Not found ticket"));
        User user = userRepository.findById(technicianId).orElseThrow(()-> new ResourceNotFoundException("Not found user"));

        if(user.getRole() != Role.TECHNICIAN){
            throw new InvalidRoleException("User is not a technician");
        }

        if(ticket.getAssignedTo() != null){
            throw new UnauthorizedActionException("Ticket already assigned");
        }

        validateTicketNotClosed(ticket);

        ticket.setAssignedTo(user);

        ticket.setStatus(TicketStatus.IN_PROGRESS);

        Ticket savedTicket = repository.save(ticket);

        return ticketMapper.entityToResponse(savedTicket);
    }

    public Page<TicketResponseDto> listMyTickets(Pageable pageable){

        Page<Ticket> ticketList = repository.findByOpenedBy(getLoggedUser(),pageable);

        return ticketList.map(ticketMapper::entityToResponse);
    }

    public Page<TicketResponseDto> listAssignedTickets(Pageable pageable) {

        Page<Ticket> assignedTickets = repository.findByAssignedTo(getLoggedUser(), pageable);

        return assignedTickets.map(ticketMapper::entityToResponse);
    }

    private LocalDateTime calculateDeadline(Priority priority){
        return switch (priority){
            case CRITICAL -> LocalDateTime.now().plusDays(1);
            case HIGH -> LocalDateTime.now().plusDays(2);
            case MEDIUM ->   LocalDateTime.now().plusDays(5);
            case LOW ->  LocalDateTime.now().plusDays(7);
        };
    }

    private void validateTicketNotClosed(Ticket ticket){
        if(ticket.getStatus()== TicketStatus.CLOSED){
            throw new TicketClosedException("The ticket cannot be updated after it has been closed.");
        }
    }

    private User getLoggedUser(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return (User) authentication.getPrincipal();
    }
}