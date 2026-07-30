package api.controller;

import api.dto.user.UserResponseDto;
import api.dto.user.UserRoleUpdateDto;
import api.dto.user.UserStatusUpdateDto;
import api.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/users")
@RequiredArgsConstructor
@RestController

public class UserController {
    private final UserService userService;

    @GetMapping
    public ResponseEntity<Page<UserResponseDto>> listAll(@PageableDefault(size = 10) Pageable pageable){
        return new ResponseEntity<>(userService.listAll(pageable), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> findById(@PathVariable Long id){
        return new ResponseEntity<>(userService.findById(id), HttpStatus.OK);
    }

    @PatchMapping("/{email}/role")
    public ResponseEntity<UserRoleUpdateDto> updateRole(@PathVariable String email,@RequestBody @Valid UserRoleUpdateDto role){
        return new ResponseEntity<>(userService.changeRole(email, role), HttpStatus.OK);
    }

    @PatchMapping("/{email}/status")
    public ResponseEntity<UserStatusUpdateDto> updateStatus(@PathVariable String email, @RequestBody @Valid UserStatusUpdateDto dto){
        return new ResponseEntity<>(userService.changeStatus(email, dto), HttpStatus.OK);
    }

}
