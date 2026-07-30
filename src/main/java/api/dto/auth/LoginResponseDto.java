package api.dto.auth;

import jakarta.validation.constraints.NotBlank;

public record LoginResponseDto (
     String token)
    {}