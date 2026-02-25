package exception;

public class ClientNotFoundException extends Exception {
    public ClientNotFoundException() {
        super();
    }
    public ClientNotFoundException(String message) {
        super(message);
    }
    @Override
    public String getMessage() {
        return "Client not found: выбранный клиент не был найден";
    }
}
