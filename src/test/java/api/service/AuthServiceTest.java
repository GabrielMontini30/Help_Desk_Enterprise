package api.service;

import api.dto.auth.LoginRequestDto;
import api.dto.auth.LoginResponseDto;
import api.dto.auth.RegisterRequestDto;
import api.dto.auth.RegisterResponseDto;
import api.entity.User;
import api.exception.EmailAlreadyExistsException;
import api.factory.UserFactory;
import api.mapper.UserMapper;
import api.repository.UserRepository;
import api.security.JwtService;
import api.util.Role;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;


@ExtendWith(SpringExtension.class)
public class AuthServiceTest {

    @Mock
    private  UserRepository repository;

    @Mock
    private  UserMapper mapper;

    @Mock
    private  BCryptPasswordEncoder encoder;

    @Mock
    private AuthenticationManager manager;

    @Mock
    private  JwtService jwtService;

    @InjectMocks
    AuthService authService;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);

        authService= new AuthService(repository, mapper, encoder,manager, jwtService);
    }

    @Test
    @DisplayName("Should register a new user when request is valid")
    void register_WhenRequestIsValid_ShouldRegisterUser(){

        RegisterRequestDto requestDto= new RegisterRequestDto("Test", "test123@gmail.com", "teste123", "TI", Role.CLIENT);

        RegisterResponseDto responseDto= new RegisterResponseDto("Test", "test123@gmail.com", "TI");
        User user= UserFactory.createValidUser();


        given(mapper.requestToUser(requestDto)).willReturn(user);
        given(mapper.requestToResponse(requestDto)).willReturn(responseDto);
        given(repository.existsByEmail(requestDto.email())).willReturn(false);
        given(repository.save(user)).willReturn(user);
        given(encoder.encode(requestDto.password())).willReturn(requestDto.password());

        RegisterResponseDto results = authService.register(requestDto);

        Assertions.assertThat(results).isNotNull();
        Assertions.assertThat(results.name()).isEqualTo(responseDto.name());
        Assertions.assertThat(results.email()).isEqualTo(responseDto.email());
        Assertions.assertThat(results.department()).isEqualTo(responseDto.department());

        then(mapper).should().requestToUser(requestDto);
        then(mapper).should().requestToResponse(requestDto);
        then(repository).should().existsByEmail(requestDto.email());
        then(repository).should().save(user);
        then(encoder).should().encode(requestDto.password());
    }


    @Test
    @DisplayName("Should throw EmailAlreadyExistsException when email already exists")
    void register_WhenEmailAlreadyExists_ShouldThrowEmailAlreadyExistsException(){

        RegisterRequestDto requestDto= new RegisterRequestDto("Test", "test123@gmail.com", "teste123", "TI", Role.CLIENT);

        given(repository.existsByEmail(requestDto.email())).willReturn(true);

        Assertions.assertThatExceptionOfType(EmailAlreadyExistsException.class).isThrownBy(()->authService.register(requestDto));

        then(repository).should().existsByEmail(requestDto.email());
    }

    @Test
    @DisplayName("Should return JWT token when credentials are valid")
    void login_WhenCredentialsAreValid_ShouldReturnJwtToken(){
        LoginRequestDto requestDto= new LoginRequestDto("emaildetest10@gmail", "teste123");
        UsernamePasswordAuthenticationToken authenticationToken= new UsernamePasswordAuthenticationToken(requestDto.email(), requestDto.password());
        Authentication authentication= authenticationToken;
        String token= "jwt-token-123";

        given(manager.authenticate(authenticationToken)).willReturn(authentication);
        given(jwtService.generatedToken(authentication.getName())).willReturn(token);

        LoginResponseDto results = authService.login(requestDto);

        Assertions.assertThat(results).isNotNull();
        Assertions.assertThat(results.token()).isEqualTo(token);

        then(manager).should().authenticate(authenticationToken);
        then(jwtService).should().generatedToken(authentication.getName());
    }
}
