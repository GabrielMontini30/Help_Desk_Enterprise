package api.dto.user;

import api.util.Role;

import java.time.LocalDateTime;

public record UserResponseDto(
        Long id,
        String name,
        String email,
        Role role,
        String department,
        Boolean active,
        LocalDateTime createdAt
) {
}
