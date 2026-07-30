package api.exception;

public class TicketClosedException extends BusinessException{
    public TicketClosedException(String message) {
        super(message);
    }
}
