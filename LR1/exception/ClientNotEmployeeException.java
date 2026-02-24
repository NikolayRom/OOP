package exception;

public class ClientNotEmployeeException extends Exception {
    public ClientNotEmployeeException() {
        super();
    }
    public ClientNotEmployeeException(String message) {
        super(message);
    }
}
