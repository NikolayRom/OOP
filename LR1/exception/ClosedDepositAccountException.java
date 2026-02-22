package exception;

public class ClosedDepositAccountException extends Exception {
    public ClosedDepositAccountException() {
        super();
    }
    public ClosedDepositAccountException(String message) {
        super(message);
    }
}
