package exception;

public class CompanyNotFoundException extends Exception {
    public CompanyNotFoundException() {
        super();
    }
    public CompanyNotFoundException(String message) {
        super(message);
    }
    @Override
    public String getMessage() {
        return "Company not found: выбранное предприятие не было найдено";
    }
}
