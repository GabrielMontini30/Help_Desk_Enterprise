package api.controller;

import api.dto.ticket.*;
import api.service.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/tickets")
@RestController
@RequiredArgsConstructor

public class TicketController {
    private final TicketService service;

    @GetMapping
    public ResponseEntity<Page<TicketResponseDto>> listAll(@PageableDefault(size = 10) Pageable pageable){
        return new ResponseEntity<>(service.listAll( pageable), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TicketResponseDto> findById(@PathVariable Long id){
        return new ResponseEntity<>(service.findById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<TicketResponseDto> create(@RequestBody @Valid TicketRequestDto dto){
        return new ResponseEntity<>(service.createTicket(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TicketResponseDto> update(@RequestBody @Valid TicketUpdateDto dto, @PathVariable Long id){
        return new ResponseEntity<>(service.updatedTicket(id, dto), HttpStatus.OK);
    }

    @PatchMapping ("/{id}/status")
    public ResponseEntity<Void> changeStatus(@RequestBody @Valid TicketStatusUpdateDto status, @PathVariable Long id) {
        service.changeStatus(id, status);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @PatchMapping ("/{id}/assign")
    public ResponseEntity<TicketResponseDto> assignTicket(@PathVariable ("id") Long ticketId , @RequestBody @Valid TicketAssignDto dto ){
        return new ResponseEntity<>(service.assignTicket(ticketId, dto.technicianId()), HttpStatus.OK);
    }

    @PatchMapping("/{id}/close")
    public ResponseEntity<Void> closeTicket(@PathVariable Long id){
        service.closeTicket(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/my")
    public ResponseEntity<Page<TicketResponseDto>> listMyTickets(@PageableDefault(size = 10) Pageable pageable){
        return new ResponseEntity<>(service.listMyTickets(pageable), HttpStatus.OK);
    }

    @GetMapping("/assigned")
    public ResponseEntity<Page<TicketResponseDto>> listAssignedTickets(@PageableDefault(size = 10) Pageable pageable){
        return new ResponseEntity<>(service.listAssignedTickets(pageable), HttpStatus.OK);
    }
}
