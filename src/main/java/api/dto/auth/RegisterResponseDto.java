package api.dto.auth;

import api.util.Department;
import io.swagger.v3.oas.annotations.media.Schema;

public record RegisterResponseDto(

        @Schema(description = "user name", example = "Hector")
        String name,

        @Schema(description = "user email", example = "emailtest@gmail.com")
        String email,

        @Schema(description = "user department", example = "IT")
        Department department) {
}
