package exception;

public class TransactionNotFoundException extends Exception {
    public TransactionNotFoundException() {
        super();
    }
    public TransactionNotFoundException(String message) {
        super(message);
    }
    @Override
    public String getMessage() {
        return "Transaction not found: выбранная транзакция не была найдена";
    }
}
