package exception;

public class NotFoundAccountException extends Exception {
    public NotFoundAccountException() {
        super();
    }
    public NotFoundAccountException(String message) {
        super(message);
    }
}
