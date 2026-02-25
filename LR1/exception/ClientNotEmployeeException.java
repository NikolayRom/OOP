package exception;

public class ClientNotEmployeeException extends Exception {
    public ClientNotEmployeeException() {
        super();
    }
    public ClientNotEmployeeException(String message) {
        super(message);
    }
    @Override
    public String getMessage() {
        return "Client not employee: невозможно выполнить операцию для клиента, не являющимся сотрудником";
    }
}
