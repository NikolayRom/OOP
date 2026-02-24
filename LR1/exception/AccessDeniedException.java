package exception;

public class AccessDeniedException extends Exception {
    public AccessDeniedException() {
        super();
    }
    public AccessDeniedException(String message) {
        super(message);
    }
}
