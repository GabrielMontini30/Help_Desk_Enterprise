package api.controller;

import api.dto.auth.LoginRequestDto;
import api.dto.auth.LoginResponseDto;
import api.dto.auth.RegisterRequestDto;
import api.dto.auth.RegisterResponseDto;
import api.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto dto){
       return new ResponseEntity<>(authService.login(dto), HttpStatus.OK);
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterResponseDto> register(@Valid @RequestBody RegisterRequestDto dto){
        return new ResponseEntity<>(authService.register(dto), HttpStatus.CREATED);
    }

}
