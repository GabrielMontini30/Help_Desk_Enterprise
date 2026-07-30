package api.exception;

public class UnauthorizedActionException extends BusinessException{
    public UnauthorizedActionException(String message) {
        super(message);
    }
}
