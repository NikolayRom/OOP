package exception;

public class InsufficientFundsException extends Exception {
    public InsufficientFundsException() {
        super();
    }
    public InsufficientFundsException(String message) {
        super(message);
    }
    @Override
    public String getMessage() {
        return "Insufficient funds: недостаточно средств на счете или вкладе";
    }
}
