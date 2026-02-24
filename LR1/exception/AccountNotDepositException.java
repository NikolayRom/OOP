package exception;

public class AccountNotDepositException extends Exception {
    public AccountNotDepositException() {
        super();
    }
    public AccountNotDepositException(String message) {
        super(message);
    }
}
