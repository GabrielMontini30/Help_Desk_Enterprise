package api.controller;

import api.dto.ticket.*;
import api.dto.user.UserResponseDto;
import api.dto.user.UserRoleUpdateDto;
import api.entity.Ticket;
import api.entity.User;
import api.factory.DtoFactory;
import api.factory.TicketFactory;
import api.factory.UserFactory;
import api.security.JwtFilter;
import api.security.JwtService;
import api.security.UserDetailsServices;
import api.service.TicketService;
import api.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(TicketController.class)
@AutoConfigureMockMvc(addFilters = false)
public class TicketControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    TicketService ticketService;

    @MockBean
    JwtFilter jwtFilter;

    @MockBean
    JwtService jwtService;

    @MockBean
    UserDetailsServices userDetailsServices;

    @Test
    @DisplayName("Should return 200 when listing all tickets")
    void listAll_ShouldReturn200() throws Exception {
        TicketResponseDto ticketResponseDto= DtoFactory.TicketResponseDto();

        Page<TicketResponseDto> page= new PageImpl<>(List.of(ticketResponseDto));

        BDDMockito.given(ticketService.listAll(any(Pageable.class))).willReturn(page);

        mockMvc.perform(get("/tickets"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(page)));
        then(ticketService).should().listAll(any(Pageable.class));
    }

    @Test
    @DisplayName("Should return 200 when ticket exists")
    void findById_ShouldReturn200() throws Exception {
        TicketResponseDto ticketResponseDto= DtoFactory.TicketResponseDto();

        BDDMockito.given(ticketService.findById(ticketResponseDto.id())).willReturn(ticketResponseDto);

        mockMvc.perform(get("/tickets/{id}",ticketResponseDto.id()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(ticketResponseDto)));
        then(ticketService).should().findById(ticketResponseDto.id());
    }

    @Test
    @DisplayName("Should return 201 when ticket is created")
    void create_ShouldReturn201() throws Exception {
        TicketRequestDto ticketRequestDto= DtoFactory.createTicketRequest();
        TicketResponseDto ticketResponseDto= DtoFactory.TicketResponseDto();

        BDDMockito.given(ticketService.createTicket(ticketRequestDto)).willReturn(ticketResponseDto);

        mockMvc.perform(post("/tickets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ticketRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(ticketResponseDto)));

        then(ticketService).should().createTicket(ticketRequestDto);
    }

    @Test
    @DisplayName("Should return 200 when ticket is updated")
    void update_ShouldReturn200() throws Exception {
        Ticket ticket= TicketFactory.createValidSavedTicket();
        TicketUpdateDto ticketUpdateDto= DtoFactory.createTicketUpdateRequest();
        TicketResponseDto responseDto= DtoFactory.TicketResponseDto();

        BDDMockito.given(ticketService.updatedTicket(ticket.getId(),ticketUpdateDto)).willReturn(responseDto);

        mockMvc.perform(put("/tickets/{id}",ticket.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ticketUpdateDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(responseDto)));

        then(ticketService).should().updatedTicket(ticket.getId(), ticketUpdateDto);
    }

    @Test
    @DisplayName("Should return 200 when ticket status is changed")
    void changeStatus_ShouldReturn204() throws Exception {
        TicketStatusUpdateDto statusUpdateDto= DtoFactory.createTicketStatusUpdate();
        Ticket ticket=TicketFactory.createValidSavedTicket();

        mockMvc.perform(patch("/tickets/{id}/status",ticket.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(statusUpdateDto)))
                .andExpect(status().isNoContent());

        then(ticketService).should().changeStatus(ticket.getId(), statusUpdateDto);
    }

    @Test
    @DisplayName("Should return 200 when ticket is assigned")
    void assignTicket_ShouldReturn200() throws Exception {
        TicketAssignDto ticketAssignDto= DtoFactory.createTicketAssign();
        TicketResponseDto ticketResponseDto=DtoFactory.TicketResponseDto();
        Ticket ticket=TicketFactory.createValidSavedTicket();

        given(ticketService.assignTicket(ticket.getId(),ticketAssignDto.technicianId())).willReturn(ticketResponseDto);

        mockMvc.perform(patch("/tickets/{id}/assign",ticket.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(ticketAssignDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(ticketResponseDto)));

        then(ticketService).should().assignTicket(ticket.getId(),ticketAssignDto.technicianId());

    }

    @Test
    @DisplayName("Should return 200 when ticket is closed")
    void closeTicket_ShouldReturn204() throws Exception {
        Ticket ticket=TicketFactory.createValidSavedTicket();

        mockMvc.perform(patch("/tickets//{id}/close", ticket.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    @DisplayName("Should return 200 when listing logged user tickets")
    void listMyTickets_ShouldReturn200() throws Exception {
        TicketResponseDto ticketResponseDto=DtoFactory.TicketResponseDto();

        Page<TicketResponseDto> page= new PageImpl<>(List.of(ticketResponseDto));

        BDDMockito.given(ticketService.listMyTickets(any(Pageable.class))).willReturn(page);

        mockMvc.perform(get("/tickets/my"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(page)));

        then(ticketService).should().listMyTickets(any(Pageable.class));
    }

    @Test
    @DisplayName("Should return 200 when listing assigned tickets")
    void listAssignedTickets_ShouldReturn200() throws Exception {
        TicketResponseDto ticketResponseDto=DtoFactory.TicketResponseDto();

        Page<TicketResponseDto> page= new PageImpl<>(List.of(ticketResponseDto));

        BDDMockito.given(ticketService.listAssignedTickets(any(Pageable.class))).willReturn(page);

        mockMvc.perform(get("/tickets/assigned"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(page)));

        then(ticketService).should().listAssignedTickets(any(Pageable.class));
    }
}
