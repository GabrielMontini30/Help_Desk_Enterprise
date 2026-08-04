package api.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;

public record RegisterResponseDto(

        @Schema(description = "user name", example = "Hector")
        String name,

        @Schema(description = "user email", example = "emailtest@gmail.com")
        String email,

        @Schema(description = "user department", example = "TI")
        String department) {
}
