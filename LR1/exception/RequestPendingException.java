package exception;

public class RequestPendingException extends Exception {
    public RequestPendingException() {
        super();
    }
    public RequestPendingException(String message) {
        super(message);
    }
    @Override
    public String getMessage() {
        return "Request pending: заявка на регистрацию еще не была обработана менеджером, ожидайте";
    }
}
