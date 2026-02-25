package exception;

public class EndDepositDurationException extends Exception {
    public EndDepositDurationException() {
        super();
    }
    public EndDepositDurationException(String message) {
        super(message);
    }
    @Override
    public String getMessage() {
        return "End deposit duration: для выбранного вклада истек срок действия";
    }
}
