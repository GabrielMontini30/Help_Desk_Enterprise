package api.controller;

import api.dto.user.UserResponseDto;
import api.dto.user.UserRoleUpdateDto;
import api.dto.user.UserStatusUpdateDto;
import api.entity.User;
import api.factory.DtoFactory;
import api.factory.UserFactory;
import api.security.JwtFilter;
import api.security.JwtService;
import api.security.UserDetailsServices;

import api.service.UserService;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Optional;


@WebMvcTest(UserController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    UserService userService;

    @MockBean
    JwtFilter jwtFilter;

    @MockBean
    JwtService jwtService;

    @MockBean
    UserDetailsServices userDetailsServices;

    @Test
    @DisplayName("")
    void listAll_ShouldReturn200() throws Exception {

        UserResponseDto responseDto= DtoFactory.userResponseDto();


        Page<UserResponseDto> page= new PageImpl<>(List.of(responseDto));

        BDDMockito.given(userService.listAll(any(Pageable.class))).willReturn(page);

        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(page)));
    }

    @Test
    @DisplayName("")
    void findById_ShouldReturn200() throws  Exception{

        UserResponseDto user= DtoFactory.userResponseDto();

        BDDMockito.given(userService.findById(user.id())).willReturn(user);

        mockMvc.perform(get("/users/{id}",user.id()))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(user)));
    }

    @Test
    @DisplayName("")
    void updateRole_ShouldReturn200() throws Exception {
        User user= UserFactory.createValidUser();
        UserRoleUpdateDto roleUpdateDto= DtoFactory.createRoleUpdate();

        BDDMockito.given(userService.changeRole(user.getEmail(),roleUpdateDto)).willReturn(roleUpdateDto);

        mockMvc.perform(patch("/users/{email}/role", user.getEmail(),roleUpdateDto.role())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(roleUpdateDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(roleUpdateDto)));

        BDDMockito.then(userService).should().changeRole(user.getEmail(),roleUpdateDto);
    }

    @Test
    @DisplayName("")
    void updateStatus_ShouldReturn200()throws Exception{
        User user= UserFactory.createValidUser();
        UserStatusUpdateDto statusUpdateDto= DtoFactory.createStatusUpdate();

        BDDMockito.given(userService.changeStatus(user.getEmail(),statusUpdateDto)).willReturn(statusUpdateDto);

        mockMvc.perform(patch("/users/{email}/status", user.getEmail(),statusUpdateDto.active())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(statusUpdateDto)))
                .andExpect(status().isOk())
                .andExpect(content().json(objectMapper.writeValueAsString(statusUpdateDto)));
    }
}
