package api.controller;

import api.dto.auth.LoginRequestDto;
import api.dto.auth.LoginResponseDto;
import api.dto.auth.RegisterRequestDto;
import api.dto.auth.RegisterResponseDto;
import api.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Authentication", description = "Endpoints for user authentication and registration.")
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "Login for users",description = "method for user login ")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User successfully logged in"),
            @ApiResponse(responseCode = "400", description = "invalid request"),
            @ApiResponse(responseCode = "401", description = "invalid email or password"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    public ResponseEntity<LoginResponseDto> login(@Valid @RequestBody LoginRequestDto dto){
       return new ResponseEntity<>(authService.login(dto), HttpStatus.OK);
    }

    @PostMapping("/register")
    @Operation(summary = "Register users",description = "method for register a new user ")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Successfully registered new user"),
            @ApiResponse(responseCode = "400", description = "invalid request"),
            @ApiResponse(responseCode = "409", description = "Email already registered"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    public ResponseEntity<RegisterResponseDto> register(@Valid @RequestBody RegisterRequestDto dto){
        return new ResponseEntity<>(authService.register(dto), HttpStatus.CREATED);
    }

}
