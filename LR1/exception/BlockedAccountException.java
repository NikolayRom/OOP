package exception;

public class BlockedAccountException extends Exception {
    public BlockedAccountException() {
        super();
    }
    public BlockedAccountException(String message) {
        super(message);
    }
    @Override
    public String getMessage() {
        return "Blocked account: невозможно выполнить операцию на заблокированном счете или вкладе";
    }
}
