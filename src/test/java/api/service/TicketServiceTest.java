package api.service;

import api.dto.ticket.TicketRequestDto;
import api.dto.ticket.TicketResponseDto;
import api.dto.ticket.TicketStatusUpdateDto;
import api.dto.ticket.TicketUpdateDto;
import api.entity.Ticket;
import api.entity.User;
import api.exception.BadRequestException;
import api.exception.InvalidRoleException;
import api.exception.TicketClosedException;
import api.factory.DtoFactory;
import api.factory.TicketFactory;
import api.factory.UserFactory;
import api.mapper.TicketMapper;
import api.repository.TicketRepository;
import api.repository.UserRepository;
import api.util.Category;
import api.util.TicketStatus;
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
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;


@ExtendWith(SpringExtension.class)
public class TicketServiceTest {


    @Mock
    private TicketMapper ticketMapper;


    @Mock
    private TicketRepository ticketRepository;


    @Mock
    private UserRepository userRepository;


    @InjectMocks
    private TicketService ticketService;



    @BeforeEach
    void setUp(){

        MockitoAnnotations.openMocks(this);

        ticketService = new TicketService(
                ticketMapper,
                ticketRepository,
                userRepository
        );
    }



    @Test
    @DisplayName("Should create ticket when request is valid")
    void createTicket_WhenRequestIsValid_ShouldCreateTicket(){


        TicketRequestDto request = DtoFactory.createTicketRequest();

        User user = UserFactory.createClient();

        Ticket ticket = TicketFactory.createOpenTicket();


        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()));


        given(ticketMapper.requestToEntity(request)).willReturn(ticket);


        given(ticketRepository.save(ticket)).willReturn(ticket);


        TicketResponseDto response = DtoFactory.TicketResponseDto();

        given(ticketMapper.entityToResponse(ticket)).willReturn(response);


        TicketResponseDto result = ticketService.createTicket(request);

        assertThat(result).isNotNull();

        assertThat(result.status()).isEqualTo(TicketStatus.OPEN);



        then(ticketRepository)
                .should()
                .save(ticket);

    }

    @Test
    @DisplayName("Should throw BadRequestException when priority is null")
    void createTicket_WhenPriorityIsNull_ShouldThrowBadRequestException(){

        TicketRequestDto request = new TicketRequestDto("Computer problem",
                "Computer does not turn on",
                null,
                Category.HARDWARE);


        User user = UserFactory.createClient();

        Ticket ticket =Ticket.builder()
                .title("Computer problem")
                .description("Computer does not turn on")
                .status(TicketStatus.OPEN)
                .priority(null)
                .category(Category.HARDWARE)
                .openedBy(user)
                .build();

        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()));

        given(ticketMapper.requestToEntity(request)).willReturn(ticket);

        assertThatExceptionOfType(BadRequestException.class).isThrownBy(()-> ticketService.createTicket(request));


    }

    @Test
    @DisplayName("Should return ticket when id exists")
    void findById_WhenTicketExists_ShouldReturnTicket(){
        Ticket ticket= TicketFactory.createValidSavedTicket();

        TicketResponseDto response = DtoFactory.TicketResponseDto();

        given(ticketRepository.findById(ticket.getId())).willReturn(Optional.of(ticket));

        given(ticketMapper.entityToResponse(ticket)).willReturn(response);

        TicketResponseDto result = ticketService.findById(ticket.getId());

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(result.title()).isEqualTo(ticket.getTitle());
        Assertions.assertThat(result.description()).isEqualTo(ticket.getDescription());

        then(ticketRepository).should().findById(ticket.getId());
        then(ticketMapper).should().entityToResponse(ticket);

    }

    @Test
    @DisplayName("Should update ticket when request is valid")
    void updatedTicket_WhenRequestIsValid_ShouldUpdateTicket(){

        Ticket ticket= TicketFactory.createValidSavedTicket();

        TicketUpdateDto ticketUpdateRequest = DtoFactory.createTicketUpdateRequest();

        TicketResponseDto response = DtoFactory.TicketResponseDto();

        given(ticketRepository.findById(ticket.getId())).willReturn(Optional.of(ticket));
        given(ticketMapper.entityToResponse(ticket)).willReturn(response);
        given(ticketRepository.save(ticket)).willReturn(ticket);

        TicketResponseDto result = ticketService.updatedTicket(ticket.getId(), ticketUpdateRequest);

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(ticket.getDescription()).isEqualTo(ticketUpdateRequest.description());
        Assertions.assertThat(ticket.getTitle()).isEqualTo(ticketUpdateRequest.title());
        Assertions.assertThat(ticket.getPriority()).isEqualTo(ticketUpdateRequest.priority());
        Assertions.assertThat(ticket.getCategory()).isEqualTo(ticketUpdateRequest.category());

        then(ticketMapper).should().entityToResponse(ticket);
        then(ticketRepository).should().save(ticket);
        then(ticketRepository).should().findById(ticket.getId());


    }

    @Test
    @DisplayName("Should throw TicketClosedException when updating a closed ticket")
    void updatedTicket_WhenTicketIsClosed_ShouldThrowTicketClosedException(){
        Ticket ticket= TicketFactory.createClosedTicket();

        TicketUpdateDto updateDto= DtoFactory.createTicketUpdateRequest();

        given(ticketRepository.findById(ticket.getId())).willReturn(Optional.of(ticket));

        assertThatExceptionOfType(TicketClosedException.class).isThrownBy(()->ticketService.updatedTicket(ticket.getId(),updateDto));

        then(ticketRepository).should().findById(ticket.getId());
    }

    @Test
    @DisplayName("Should close ticket")
    void closeTicket_WhenTicketExists_ShouldCloseTicket(){
        Ticket ticket= TicketFactory.createValidSavedTicket();

        given(ticketRepository.findById(ticket.getId())).willReturn(Optional.of(ticket));
        given(ticketRepository.save(ticket)).willReturn(ticket);

        Assertions.assertThatCode(()-> ticketService.closeTicket(ticket.getId())).doesNotThrowAnyException();
        Assertions.assertThat(ticket.getStatus()).isEqualTo(TicketStatus.CLOSED);

        then(ticketRepository).should().findById(ticket.getId());

        then(ticketRepository).should().save(ticket);
    }

    @Test
    @DisplayName("Should change ticket status")
    void changeStatus_WhenRequestIsValid_ShouldChangeStatus(){
        Ticket ticket= TicketFactory.createValidSavedTicket();

        TicketStatusUpdateDto updateDto= DtoFactory.createTicketStatusUpdate();

        given(ticketRepository.findById(ticket.getId())).willReturn(Optional.of(ticket));
        given(ticketRepository.save(ticket)).willReturn(ticket);

        ticketService.changeStatus(ticket.getId(), updateDto);

        assertThat(ticket.getStatus()).isEqualTo(updateDto.status());

        then(ticketRepository).should().findById(ticket.getId());
        then(ticketRepository).should().save(ticket);
    }

    @Test
    @DisplayName("Should assign technician to ticket")
    void assignTicket_WhenRequestIsValid_ShouldAssignTechnician(){
        Ticket ticket= TicketFactory.createValidSavedTicket();
        TicketResponseDto responseDto= DtoFactory.TicketResponseDto();
        User user= UserFactory.createTechnician();

        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
        given(ticketRepository.findById(ticket.getId())).willReturn(Optional.of(ticket));
        given(ticketRepository.save(ticket)).willReturn(ticket);
        given(ticketMapper.entityToResponse(ticket)).willReturn(responseDto);

        TicketResponseDto result = ticketService.assignTicket(ticket.getId(), user.getId());

        Assertions.assertThat(result).isNotNull();
        Assertions.assertThat(ticket.getAssignedTo()).isEqualTo(user);
        Assertions.assertThat(ticket.getStatus()).isEqualTo(TicketStatus.IN_PROGRESS);

        then(ticketRepository).should().findById(ticket.getId());
        then(ticketRepository).should().save(ticket);
        then(ticketMapper).should().entityToResponse(ticket);
        then(userRepository).should().findById(user.getId());
    }

    @Test
    @DisplayName("Should throw InvalidRoleException when user is not a technician")
    void assignTicket_WhenUserIsNotTechnician_ShouldThrowInvalidRoleException(){
        Ticket ticket= TicketFactory.createValidSavedTicket();

        User user= UserFactory.createValidUser();

        given(userRepository.findById(user.getId())).willReturn(Optional.of(user));
        given(ticketRepository.findById(ticket.getId())).willReturn(Optional.of(ticket));

       assertThatExceptionOfType(InvalidRoleException.class).isThrownBy(()-> ticketService.assignTicket(ticket.getId(),user.getId()));

        then(ticketRepository).should().findById(ticket.getId());
        then(userRepository).should().findById(user.getId());
    }

    @Test
    @DisplayName("Should return logged user's tickets")
    void listMyTickets_WhenUserHasTickets_ShouldReturnPage(){
        Ticket ticket= TicketFactory.createValidSavedTicket();

        User user= UserFactory.createValidUser();

        ticket.setOpenedBy(user);

        Pageable pageable = PageRequest.of(0, 10);

        Page<Ticket> ticketPage = new PageImpl<>(List.of(ticket), pageable, 1);

        TicketResponseDto responseDto = DtoFactory.TicketResponseDto();

        SecurityContextHolder.getContext().setAuthentication(
                        new UsernamePasswordAuthenticationToken(
                                user,
                                null,
                                user.getAuthorities()
                        )
                );

        given(ticketRepository.findByOpenedBy(user, pageable)).willReturn(ticketPage);

        given(ticketMapper.entityToResponse(ticket)).willReturn(responseDto);

        Page<TicketResponseDto> result = ticketService.listMyTickets(pageable);

        Assertions.assertThat(result).isNotNull();

        Assertions.assertThat(result.getContent()).hasSize(1);

        Assertions.assertThat(result.getContent().getFirst()).isEqualTo(responseDto);

        then(ticketRepository).should().findByOpenedBy(user, pageable);

        then(ticketMapper).should().entityToResponse(ticket);
    }
}
