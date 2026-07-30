package api.factory;

import api.dto.auth.RegisterRequestDto;
import api.dto.comment.CommentRequestDto;
import api.dto.ticket.*;
import api.dto.user.UserResponseDto;
import api.dto.user.UserRoleUpdateDto;
import api.dto.user.UserStatusUpdateDto;
import api.entity.Ticket;
import api.entity.User;
import api.util.Category;
import api.util.Priority;
import api.util.Role;
import api.util.TicketStatus;

import java.time.LocalDateTime;

public class DtoFactory {

    public static RegisterRequestDto createRegisterRequest(){

        return new RegisterRequestDto(
                "Gabriel Test",
                "gabriel@test.com",
                "123456",
                "IT",
                Role.CLIENT
        );

    }


    public static UserResponseDto userResponseDto(){
        UserResponseDto responseDto= new UserResponseDto(
                1L,
                "Gabriel Test",
                "gabriel@test.com",
                Role.CLIENT,
                "TI",
                true,
                LocalDateTime.now());

        return responseDto;
    }

    public static TicketResponseDto TicketResponseDto(){

        Ticket ticket= TicketFactory.createValidSavedTicket();

        User user= UserFactory.createClient();

        TicketResponseDto ticketResponseDto = new TicketResponseDto(1L,
                "Computer problem",
                "Computer does not turn on",
                TicketStatus.OPEN,
                ticket.getPriority(),
                ticket.getCategory(),
                null,
                user.getEmail(),
                null,
                null,
                ticket.getSlaDeadline()
        );

        return ticketResponseDto;
    }

    public static TicketRequestDto createTicketRequest(){

        return new TicketRequestDto(
                "Computer problem",
                "Computer does not turn on",
                Priority.MEDIUM,
                Category.HARDWARE
        );

    }



    public static TicketUpdateDto createTicketUpdateRequest(){

        return new TicketUpdateDto(
                "Updated title",
                "Updated description",
                Priority.HIGH,
                Category.SOFTWARE
        );
    }



    public static TicketStatusUpdateDto createTicketStatusUpdate(){

        return new TicketStatusUpdateDto(
                TicketStatus.IN_PROGRESS
        );

    }



    public static TicketAssignDto createTicketAssign(){

        return new TicketAssignDto(
                1L
        );

    }



    public static CommentRequestDto createCommentRequest(){

        return new CommentRequestDto(
                "Public comment",
                false
        );

    }



    public static UserRoleUpdateDto createRoleUpdate(){

        return new UserRoleUpdateDto(
                Role.TECHNICIAN
        );

    }



    public static UserStatusUpdateDto createStatusUpdate(){

        return new UserStatusUpdateDto(
                true
        );

    }
}
