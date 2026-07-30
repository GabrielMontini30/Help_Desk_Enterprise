package api.factory;

import api.entity.Comment;

public class CommentFactory {

    public static Comment createValidComment(){

    return Comment.builder()
            .id(1L)
            .message("Comment test")
            .author(UserFactory.createTechnician())
            .ticket(TicketFactory.createOpenTicket())
            .internal(false)
            .build();
}

    public static Comment createValidCommentWithDifferentUserId(){

        return Comment.builder()
                .id(1L)
                .message("Comment test")
                .author(UserFactory.createTechnicianDifferentId())
                .ticket(TicketFactory.createOpenTicket())
                .internal(false)
                .build();
    }


    public static Comment createInternalComment(){

        return Comment.builder()
                .id(1L)
                .message("Internal comment")
                .author(UserFactory.createTechnician())
                .ticket(TicketFactory.createOpenTicket())
                .internal(true)
                .build();

    }



    public static Comment createPublicComment(){

        return Comment.builder()
                .id(1L)
                .message("Public comment")
                .author(UserFactory.createClient())
                .ticket(TicketFactory.createOpenTicket())
                .internal(false)
                .build();

    }
}
