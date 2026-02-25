package exception;

public class DropFromCompanyException extends Exception {
    public DropFromCompanyException() {
        super();
    }
    public DropFromCompanyException(String message) {
        super(message);
    }
    @Override
    public String getMessage() {
        return "Drop from company: невозможно уволить выбранного клиента из данного предприятия";
    }
}
