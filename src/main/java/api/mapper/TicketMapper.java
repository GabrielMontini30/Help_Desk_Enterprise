package api.mapper;

import api.dto.ticket.TicketAssignDto;
import api.dto.ticket.TicketRequestDto;
import api.dto.ticket.TicketResponseDto;
import api.dto.ticket.TicketUpdateDto;
import api.entity.Ticket;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.data.domain.Page;

@Mapper(componentModel = "spring")
public interface TicketMapper {

    @Mapping(target = "openedBy", ignore = true)
    @Mapping(target = "assignedTo", ignore = true)
    Ticket requestToEntity(TicketRequestDto dto);

    @Mapping(target = "openedBy", source = "openedBy.name")
    @Mapping(target = "assignedTo", source = "assignedTo.name")
    TicketResponseDto entityToResponse(Ticket ticket);

}
