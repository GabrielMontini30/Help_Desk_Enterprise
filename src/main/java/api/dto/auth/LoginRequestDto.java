package api.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDto(
    @Schema(description = "user email", example = "emailteste@gmail.com")
    @Email
    String email,

    @Schema(description = "user password", example = "email123")
    @NotBlank(message = "The password not blank")
    String password) {

}