package exception;

public class BlockedAccountException extends Exception {
    public BlockedAccountException() {
        super();
    }
    public BlockedAccountException(String message) {
        super(message);
    }
}
