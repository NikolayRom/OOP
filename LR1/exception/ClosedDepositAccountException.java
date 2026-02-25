package exception;

public class ClosedDepositAccountException extends Exception {
    public ClosedDepositAccountException() {
        super();
    }
    public ClosedDepositAccountException(String message) {
        super(message);
    }
    @Override
    public String getMessage() {
        return "Closed deposit account: невозможно выполнить операцию для вклада до истечения срока";
    }
}
