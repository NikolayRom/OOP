package exception;

public class SalaryProjectNotFoundException extends Exception {
    public SalaryProjectNotFoundException() {
        super();
    }
    public SalaryProjectNotFoundException(String message) {
        super(message);
    }
    @Override
    public String getMessage() {
        return "Salary project not found: данный клиент не имеет оплачиваемый проект";
    }
}
