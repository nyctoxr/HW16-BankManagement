package exceptions;

public class InvalidCardDetailsException extends Exception{
    public InvalidCardDetailsException(String message) {
        super(message);
    }
}
