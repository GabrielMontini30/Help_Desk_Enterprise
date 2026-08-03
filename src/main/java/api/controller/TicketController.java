package api.controller;

import api.dto.ticket.*;
import api.service.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Tickets", description = "Endpoint for managing support tickets.")
public class TicketController {
    private final TicketService service;

    @GetMapping
    @Operation(summary = "List tickets", description = "Returns a paginated list of tickets.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tickets returned successfully"),
            @ApiResponse(responseCode = "500", description = "server error")
    })
    public ResponseEntity<Page<TicketResponseDto>> listAll(@PageableDefault(size = 10) Pageable pageable){
        return new ResponseEntity<>(service.listAll( pageable), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Find by ticket id", description = "Method for find by ticket id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = " Ticket found"),
            @ApiResponse(responseCode = "400", description = "Invalid ticket id"),
            @ApiResponse(responseCode = "404", description = "Ticket not found"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    public ResponseEntity<TicketResponseDto> findById(@PathVariable Long id){
        return new ResponseEntity<>(service.findById(id), HttpStatus.OK);
    }

    @PostMapping
    @Operation(summary = "Create a ticket", description = "Method for create a new ticket")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Ticket created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid ticket request"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    public ResponseEntity<TicketResponseDto> create(@RequestBody @Valid TicketRequestDto dto){
        return new ResponseEntity<>(service.createTicket(dto), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    @Operation(summary = "update ticket",    description = "Updates an existing ticket.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ticket updated successfully "),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Ticket not found"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    public ResponseEntity<TicketResponseDto> update(@RequestBody @Valid TicketUpdateDto dto, @PathVariable Long id){
        return new ResponseEntity<>(service.updatedTicket(id, dto), HttpStatus.OK);
    }

    @PatchMapping ("/{id}/status")
    @Operation(summary = "update ticket status",    description = "Updates the status of an existing ticket. Technician access only")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully updated a status "),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Ticket not found"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    public ResponseEntity<Void> changeStatus(@RequestBody @Valid TicketStatusUpdateDto status, @PathVariable Long id) {
        service.changeStatus(id, status);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PatchMapping ("/{id}/assign")
    @Operation(summary = "Assign tickets", description = "Assigns a technician to an existing ticket")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Ticket assigned successfully "),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Ticket not found"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    public ResponseEntity<TicketResponseDto> assignTicket(@PathVariable ("id") Long ticketId , @RequestBody @Valid TicketAssignDto dto ){
        return new ResponseEntity<>(service.assignTicket(ticketId, dto.technicianId()), HttpStatus.OK);
    }

    @PatchMapping("/{id}/close")
    @Operation(summary = "Closed tickets", description = "Closes an existing ticket. Admin access only")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "No content closed a ticket "),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "Ticket not found"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    public ResponseEntity<Void> closeTicket(@PathVariable Long id){
        service.closeTicket(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/my")
    @Operation(summary = "List my tickets", description = "Returns a paginated list of my tickets.")    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "return my tickets"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "500", description = "server error")
    })
    public ResponseEntity<Page<TicketResponseDto>> listMyTickets(@PageableDefault(size = 10) Pageable pageable){
        return new ResponseEntity<>(service.listMyTickets(pageable), HttpStatus.OK);
    }

    @GetMapping("/assigned")
    @Operation(summary = "List assigned tickets", description = "Returns a paginated list of assigned tickets.")    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "return assigned tickets"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "500", description = "server error")
    })
    public ResponseEntity<Page<TicketResponseDto>> listAssignedTickets(@PageableDefault(size = 10) Pageable pageable){
        return new ResponseEntity<>(service.listAssignedTickets(pageable), HttpStatus.OK);
    }
}
