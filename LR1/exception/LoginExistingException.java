package exception;

public class LoginExistingException extends Exception {
    public LoginExistingException() {
        super();
    }
    public LoginExistingException(String message) {
        super(message);
    }
}
