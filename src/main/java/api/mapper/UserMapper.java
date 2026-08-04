package api.mapper;

import api.dto.auth.RegisterRequestDto;
import api.dto.auth.RegisterResponseDto;
import api.dto.user.UserResponseDto;
import api.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User requestToUser(RegisterRequestDto dto);

    RegisterResponseDto requestToResponse(RegisterRequestDto dto);

    RegisterResponseDto userToResponse(User user);

    UserResponseDto userToUserResponse(User user);
}
