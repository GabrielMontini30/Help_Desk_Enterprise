package api.dto.auth;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record LoginRequestDto(

    @Email
    String email,

    @NotBlank(message = "The password not blank")
    String password) {

}