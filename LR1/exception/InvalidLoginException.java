package exception;

public class InvalidLoginException extends Exception {
    public InvalidLoginException() {
        super();
    }
    public InvalidLoginException(String message) {
        super(message);
    }
    @Override
    public String getMessage() {
        return "Invalid login: введен неправильный логин или пароль";
    }
}
