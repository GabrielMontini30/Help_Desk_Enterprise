package api.service;

import api.dto.auth.LoginRequestDto;
import api.dto.auth.LoginResponseDto;
import api.dto.auth.RegisterRequestDto;
import api.dto.auth.RegisterResponseDto;
import api.entity.User;
import api.exception.EmailAlreadyExistsException;
import api.mapper.UserMapper;
import api.repository.UserRepository;
import api.security.JwtService;
import api.util.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository repository;
    private final UserMapper mapper;
    private final BCryptPasswordEncoder encoder;
    private final AuthenticationManager manager;
    private final JwtService jwtService;

    public LoginResponseDto login(LoginRequestDto dto){

        Authentication authenticate = manager.authenticate(new UsernamePasswordAuthenticationToken(dto.email(), dto.password()));

        String token = jwtService.generatedToken(authenticate.getName());

        return new LoginResponseDto(token);
    }

    public RegisterResponseDto register(RegisterRequestDto dto){

        if(repository.existsByEmail(dto.email())) throw new EmailAlreadyExistsException("the email exists, please try again!");

        User userToSave = mapper.requestToUser(dto);

        userToSave.setPassword(encoder.encode(dto.password()));
        userToSave.setActive(true);
        userToSave.setName(dto.name());
        userToSave.setEmail(dto.email());
        userToSave.setDepartment(dto.department());
        userToSave.setRole(Role.CLIENT);

      repository.save(userToSave);

      return mapper.requestToResponse(dto);

    }
}
