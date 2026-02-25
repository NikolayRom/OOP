package exception;

public class BankNotFoundException extends Exception {
    public BankNotFoundException() {
        super();
    }
    public BankNotFoundException(String message) {
        super(message);
    }
    @Override
    public String getMessage() {
        return "Bank not found: не найден выбранный банк";
    }
}
