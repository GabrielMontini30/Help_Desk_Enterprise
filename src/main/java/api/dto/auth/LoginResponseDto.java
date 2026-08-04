package api.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;

public record LoginResponseDto (

        @Schema(description = "jwt access token", example = "eyJhbGciOiJIUzI1NiJ9..")
        String token)
    {}