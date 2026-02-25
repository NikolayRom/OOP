package exception;

public class ManagerNotFoundException extends Exception {
    public ManagerNotFoundException() {
        super();
    }
    public ManagerNotFoundException(String message) {
        super(message);
    }
    @Override
    public String getMessage() {
        return "Manager not found: выбранный менеджер не был найден";
    }
}
