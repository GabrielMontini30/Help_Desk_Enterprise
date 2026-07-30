package api.controller;

import api.dto.auth.LoginRequestDto;
import api.dto.auth.LoginResponseDto;
import api.dto.auth.RegisterRequestDto;
import api.dto.auth.RegisterResponseDto;
import api.factory.DtoFactory;
import api.security.JwtFilter;
import api.security.JwtService;
import api.security.UserDetailsServices;
import api.service.AuthService;
import api.util.Role;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.event.AuthenticationFailureServiceExceptionEvent;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthControllerTest {
    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    AuthService authService;

    @MockBean
    JwtFilter jwtFilter;

    @MockBean
    JwtService jwtService;

    @MockBean
    UserDetailsServices userDetailsServices;


    @Test
    @DisplayName("Should return 200 when login credentials are valid")
    void login_ShouldReturn200() throws Exception {
        LoginRequestDto requestDto= new LoginRequestDto("emaildetest10@gmail", "teste123");

        LoginResponseDto responseDto= new LoginResponseDto("jwt-token");

        BDDMockito.given(authService.login(requestDto)).willReturn(responseDto);

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token"));
    }

    @Test
    @DisplayName("Should return 201 when register credentials are valid")
    void register_ShouldReturn201() throws Exception {
        RegisterRequestDto requestDto= new RegisterRequestDto("Test", "test123@gmail.com", "teste123", "TI", Role.CLIENT);

        RegisterResponseDto responseDto= new RegisterResponseDto("Test", "test123@gmail.com", "TI");

        BDDMockito.given(authService.register(requestDto)).willReturn(responseDto);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestDto)))
                .andExpect(status().isCreated())
                .andExpect(content().json(objectMapper.writeValueAsString(responseDto)));
    }
}
