package exception;

public class NotFoundAccountException extends Exception {
    public NotFoundAccountException() {
        super();
    }
    public NotFoundAccountException(String message) {
        super(message);
    }
    @Override
    public String getMessage() {
        return "Account not found: выбранный счет или вклад не был найден";
    }
}
