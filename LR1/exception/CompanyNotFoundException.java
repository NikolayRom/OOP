package exception;

public class CompanyNotFoundException extends Exception {
    public CompanyNotFoundException() {
        super();
    }
    public CompanyNotFoundException(String message) {
        super(message);
    }
}
