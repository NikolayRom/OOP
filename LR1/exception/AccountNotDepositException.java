package exception;

public class AccountNotDepositException extends Exception {
    public AccountNotDepositException() {
        super();
    }
    public AccountNotDepositException(String message) {
        super(message);
    }
    @Override
    public String getMessage() {
        return "Account not deposit: невозможно выполнить данную операцию для не вклада";
    }
}
