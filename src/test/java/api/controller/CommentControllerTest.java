package api.controller;

import api.dto.comment.CommentRequestDto;
import api.dto.comment.CommentResponseDto;
import api.dto.ticket.TicketResponseDto;
import api.entity.Comment;
import api.entity.Ticket;
import api.entity.User;
import api.factory.CommentFactory;
import api.factory.DtoFactory;
import api.factory.TicketFactory;
import api.factory.UserFactory;
import api.security.JwtFilter;
import api.security.JwtService;
import api.security.UserDetailsServices;
import api.service.CommentService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.h2.schema.UserDefinedFunction;
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;


import java.time.LocalDateTime;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.BDDMockito.then;
import static org.mockito.BDDMockito.given;


@WebMvcTest(CommentController.class)
@AutoConfigureMockMvc(addFilters = false)
public class CommentControllerTest {


    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    CommentService commentService;

    @MockBean
    JwtFilter jwtFilter;

    @MockBean
    JwtService jwtService;

    @MockBean
    UserDetailsServices userDetailsServices;

    @Test
    @DisplayName("Should return 201 when comment is created")
    void create_ShouldReturn201() throws Exception {
        CommentRequestDto commentRequestDto= DtoFactory.createCommentRequest();
        User user= UserFactory.createValidUser();
        Ticket ticket= TicketFactory.createValidSavedTicket();
        CommentResponseDto commentResponseDto= new CommentResponseDto(1L,"Public comment", user ,false, LocalDateTime.now());

        given(commentService.create(commentRequestDto, ticket.getId())).willReturn(commentResponseDto);

        mockMvc.perform(post("/tickets/{ticketId}/comments",ticket.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(commentRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(commentResponseDto)));

        then(commentService).should().create(commentRequestDto,ticket.getId());
    }


    @Test
    @DisplayName("Should return 200 when listing ticket comments")
    void listAllByTicket_ShouldReturn200() throws Exception {
        Ticket ticket= TicketFactory.createValidSavedTicket();
        User user= UserFactory.createValidUser();
        CommentResponseDto commentResponseDto= new CommentResponseDto(1L,"Public comment", user ,false, LocalDateTime.now());

        Page<CommentResponseDto> page= new PageImpl<>(List.of(commentResponseDto));

        given(commentService.listAllByTicket(eq(ticket.getId()),any(Pageable.class))).willReturn(page);

        mockMvc.perform(get("/tickets/{ticketId}/comments",ticket.getId()))
                .andExpect(status().isOk());

        then(commentService).should().listAllByTicket(eq(ticket.getId()), any(Pageable.class));
    }

    @Test
    @DisplayName("Should return 200 when comment is updated")
    void update_ShouldReturn200() throws Exception {
        CommentRequestDto commentRequestDto= DtoFactory.createCommentRequest();
        User user= UserFactory.createValidUser();
        Ticket ticket= TicketFactory.createValidSavedTicket();
        Comment comment= CommentFactory.createValidComment();
        CommentResponseDto commentResponseDto= new CommentResponseDto(1L,"Public comment", user ,false, LocalDateTime.now());

        given(commentService.update(comment.getId(),commentRequestDto)).willReturn(commentResponseDto);

        mockMvc.perform(put("/comments/{commentId}",comment.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(commentRequestDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(commentResponseDto)));


        then(commentService).should().update(comment.getId(),commentRequestDto);
    }


    @Test
    @DisplayName("Should return 204 when comment is deleted")
    void delete_ShouldReturn204() throws Exception {
        Comment comment=CommentFactory.createValidComment();

        mockMvc.perform(delete("/comments/{commentId}",comment.getId()))
                .andExpect(status().isNoContent());
    }

}
