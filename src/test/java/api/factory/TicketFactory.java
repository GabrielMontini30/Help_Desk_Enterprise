package api.factory;

import api.entity.Ticket;
import api.entity.User;
import api.util.Category;
import api.util.Priority;
import api.util.TicketStatus;

public class TicketFactory {

        public static Ticket createValidTicket(){

            User user = UserFactory.createClient();

            return Ticket.builder()
                    .title("Computer problem")
                    .description("Computer does not turn on")
                    .status(TicketStatus.OPEN)
                    .priority(Priority.MEDIUM)
                    .category(Category.HARDWARE)
                    .openedBy(user)
                    .build();

        }


        public static Ticket createOpenTicket(){

            return Ticket.builder()
                    .id(1L)
                    .title("Open ticket")
                    .description("Testing open ticket")
                    .status(TicketStatus.OPEN)
                    .priority(Priority.LOW)
                    .category(Category.SOFTWARE)
                    .openedBy(UserFactory.createClient())
                    .build();

        }



        public static Ticket createClosedTicket(){

            return Ticket.builder()
                    .id(1L)
                    .title("Closed ticket")
                    .description("Testing closed ticket")
                    .status(TicketStatus.CLOSED)
                    .priority(Priority.HIGH)
                    .category(Category.HARDWARE)
                    .openedBy(UserFactory.createClient())
                    .build();

        }

    public static Ticket createValidSavedTicket() {
        return Ticket.builder()
                .id(1L)
                .title("Computer problem")
                .description("Computer does not turn on")
                .status(TicketStatus.OPEN)
                .priority(Priority.HIGH)
                .category(Category.HARDWARE)
                .openedBy(UserFactory.createClient())
                .build();
    }



        public static Ticket createAssignedTicket(){

            return Ticket.builder()
                    .title("Assigned ticket")
                    .description("Ticket with technician")
                    .status(TicketStatus.OPEN)
                    .priority(Priority.HIGH)
                    .category(Category.NETWORK)
                    .openedBy(UserFactory.createClient())
                    .assignedTo(UserFactory.createTechnician())
                    .build();

        }

    }
