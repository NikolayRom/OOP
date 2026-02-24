package exception;

public class BankNotFoundException extends Exception {
    public BankNotFoundException() {
        super();
    }
    public BankNotFoundException(String message) {
        super(message);
    }
}
