package api.service;

import api.dto.user.UserResponseDto;
import api.dto.user.UserRoleUpdateDto;
import api.dto.user.UserStatusUpdateDto;
import api.entity.Ticket;
import api.entity.User;
import api.exception.ResourceNotFoundException;
import api.factory.DtoFactory;
import api.factory.UserFactory;
import api.mapper.UserMapper;
import api.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)

public class UserServiceTest {
    @Mock
    private  UserRepository userRepository;

    @Mock
    private  UserMapper userMapper;

    @InjectMocks
    UserService userService;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);

        userService = new UserService(userRepository,userMapper);
    }

   @Test
   @DisplayName("Should return a page of users")
   void listAll_ShouldReturnPageOfUsers(){
       User user = UserFactory.createValidUser();

       UserResponseDto responseDto= DtoFactory.userResponseDto();

       Pageable pageable = PageRequest.of(0, 10);

       Page<User> userPage = new PageImpl<>(List.of(user), pageable, 1);

       given(userRepository.findAll(pageable)).willReturn(userPage);
       given(userMapper.userToUserResponse(user)).willReturn(responseDto);

       Page<UserResponseDto> results = userService.listAll(pageable);

       Assertions.assertThat(results.getContent()).hasSize(1);

       Assertions.assertThat(results.getContent().getFirst()).isEqualTo(responseDto);

       then(userRepository).should().findAll(pageable);

       then(userMapper).should().userToUserResponse(user);
   }

    @Test
    @DisplayName("Should return user when id exists")
    void findById_WhenUserExists_ShouldReturnUser(){
        User user= UserFactory.createValidUser();

        UserResponseDto userResponseDto= DtoFactory.userResponseDto();

        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
        given(userMapper.userToUserResponse(user)).willReturn(userResponseDto);

        UserResponseDto results = userService.findById(user.getId());

        Assertions.assertThat(results).isNotNull();
        Assertions.assertThat(results.id()).isEqualTo(user.getId());
        Assertions.assertThat(results.email()).isEqualTo(user.getEmail());
        Assertions.assertThat(results.name()).isEqualTo(user.getName());
        Assertions.assertThat(results.department()).isEqualTo(user.getDepartment());

        then(userMapper).should().userToUserResponse(user);
        then(userRepository).should().findById(user.getId());

    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when user does not exist")
    void findById_WhenUserDoesNotExist_ShouldThrowResourceNotFoundException(){

        given(userRepository.findById(anyLong())).willReturn(Optional.empty());

        Assertions.assertThatExceptionOfType(ResourceNotFoundException.class).isThrownBy(()->userService.findById(1L));

        then(userRepository).should().findById(1L);
    }

    @Test
    @DisplayName("Should update user role when request is valid")
    void changeRole_WhenRequestIsValid_ShouldUpdateRole(){
        User user= UserFactory.createValidUser();

        UserRoleUpdateDto roleUpdateDto= DtoFactory.createRoleUpdate();

        given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.of(user));
        given(userRepository.save(user)).willReturn(user);

        UserRoleUpdateDto results = userService.changeRole(user.getEmail(), roleUpdateDto);

        Assertions.assertThat(results).isNotNull();
        Assertions.assertThat(results.role()).isEqualTo(roleUpdateDto.role());
        Assertions.assertThat(user.getRole()).isEqualTo(roleUpdateDto.role());

        then(userRepository).should().findByEmail(user.getEmail());
        then(userRepository).should().save(user);
    }

    @Test
    @DisplayName("Should update user status when request is valid")
    void changeStatus_WhenUserExists_ShouldUpdateStatus(){
        User user= UserFactory.createValidUser();

        UserStatusUpdateDto statusUpdateDto= DtoFactory.createStatusUpdate();

        given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.of(user));
        given(userRepository.save(user)).willReturn(user);

        UserStatusUpdateDto results = userService.changeStatus(user.getEmail(), statusUpdateDto);

        Assertions.assertThat(results).isNotNull();
        Assertions.assertThat(results.active()).isEqualTo(statusUpdateDto.active());
        Assertions.assertThat(user.isActive()).isEqualTo(statusUpdateDto.active());

        then(userRepository).should().findByEmail(user.getEmail());
        then(userRepository).should().save(user);
    }

}
