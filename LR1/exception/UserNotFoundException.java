package exception;

public class UserNotFoundException extends Exception {
    public UserNotFoundException() {
        super();
    }
    public UserNotFoundException(String message) {
        super(message);
    }
    @Override
    public String getMessage() {
        return "User not found: выбранный пользователь не был найден";
    }
}
