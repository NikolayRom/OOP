package exception;

public class ManagerNotFoundException extends Exception {
    public ManagerNotFoundException() {
        super();
    }
    public ManagerNotFoundException(String message) {
        super(message);
    }
}
