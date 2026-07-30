package api.service;

import api.dto.user.UserResponseDto;
import api.dto.user.UserRoleUpdateDto;
import api.dto.user.UserStatusUpdateDto;
import api.entity.User;
import api.exception.ResourceNotFoundException;
import api.mapper.UserMapper;
import api.repository.UserRepository;
import api.util.Role;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;
    private final UserMapper mapper;

    public Page<UserResponseDto> listAll(Pageable pageable){
        Page<User> listUsers = repository.findAll(pageable);

        return listUsers.map(mapper::userToUserResponse);
    }

    public UserResponseDto findById(Long id){
        User userFound = repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("The id not found anything"));
        return mapper.userToUserResponse(userFound);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public UserRoleUpdateDto changeRole(String email,UserRoleUpdateDto roleToUpdate){
        User userFound = repository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("The email not found"));

        userFound.setRole(roleToUpdate.role());

        repository.save(userFound);

        return new UserRoleUpdateDto(userFound.getRole());
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public UserStatusUpdateDto changeStatus(String email, UserStatusUpdateDto activeToUpdate){
        User userFound = repository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException("The email not found"));

        userFound.setActive(activeToUpdate.active());

        repository.save(userFound);

        return new UserStatusUpdateDto(userFound.isEnabled());
    }
}
