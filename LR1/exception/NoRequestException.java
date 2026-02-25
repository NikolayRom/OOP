package exception;

public class NoRequestException extends Exception {
    public NoRequestException() {
        super();
    }
    public NoRequestException(String message) {
        super(message);
    }
    @Override
    public String getMessage() {
        return "No request: нет запросов, обрабатывать нечего";
    }
}
