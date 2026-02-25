package exception;

public class InvalidDurationInMonthException extends Exception {
    public InvalidDurationInMonthException() {
        super();
    }
    public InvalidDurationInMonthException(String message) {
        super(message);
    }
    @Override
    public String getMessage() {
        return "Invalid duration in month input: некорректное значение для срока действия(Integer value, 1 <= durationInMonth)";
    }
}
