package api.controller;

import api.dto.user.UserResponseDto;
import api.dto.user.UserRoleUpdateDto;
import api.dto.user.UserStatusUpdateDto;
import api.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Users", description = "endpoints to search and update users")
public class UserController {
    private final UserService userService;

    @GetMapping
    @Operation(summary = "List users", description = "Returns a paginated list of users.")    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "return all users"),
            @ApiResponse(responseCode = "500", description = "server error")
    })
    public ResponseEntity<Page<UserResponseDto>> listAll(@PageableDefault(size = 10) Pageable pageable){
        return new ResponseEntity<>(userService.listAll(pageable), HttpStatus.OK);
    }

    @Operation(summary = "Find by user id", description = "Method for find by user id")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "User founded"),
            @ApiResponse(responseCode = "400", description = "invalid user id"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> findById(@PathVariable Long id){
        return new ResponseEntity<>(userService.findById(id), HttpStatus.OK);
    }

    @Operation(summary = "update user role",    description = "Updates the role of an existing user. Requires ADMIN permission.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully updated a role "),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @PatchMapping("/{email}/role")
    public ResponseEntity<UserRoleUpdateDto> updateRole(@PathVariable String email,@RequestBody @Valid UserRoleUpdateDto role){
        return new ResponseEntity<>(userService.changeRole(email, role), HttpStatus.OK);
    }

    @Operation(summary = "update user status",    description = "Updates the status of an existing user. Requires ADMIN permission.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Successfully updated a status "),
            @ApiResponse(responseCode = "400", description = "Invalid request"),
            @ApiResponse(responseCode = "401", description = "Authentication required"),
            @ApiResponse(responseCode = "403", description = "Access denied"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "500", description = "Server error")
    })
    @PatchMapping("/{email}/status")
    public ResponseEntity<UserStatusUpdateDto> updateStatus(@PathVariable String email, @RequestBody @Valid UserStatusUpdateDto dto){
        return new ResponseEntity<>(userService.changeStatus(email, dto), HttpStatus.OK);
    }

}
