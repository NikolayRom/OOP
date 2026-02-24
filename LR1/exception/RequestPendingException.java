package exception;

public class RequestPendingException extends Exception {
    public RequestPendingException() {
        super();
    }
    public RequestPendingException(String message) {
        super(message);
    }
}
