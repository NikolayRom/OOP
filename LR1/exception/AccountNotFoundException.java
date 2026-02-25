package exception;

public class AccountNotFoundException extends Exception {
    public AccountNotFoundException() {
        super();
    }
    public AccountNotFoundException(String message) {
        super(message);
    }
    @Override
    public String getMessage() {
        return "Account not found: не найден выбранный счет или вклад";
    }
}
