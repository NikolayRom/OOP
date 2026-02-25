package exception;

public class AccessDeniedException extends Exception {
    public AccessDeniedException() {
        super();
    }
    public AccessDeniedException(String message) {
        super(message);
    }
    @Override
    public String getMessage() {
        return "Access Denied: выбранный аккаунт заблокирован или недоступен";
    }
}
