package exception;

public class InvalidAmountInputException extends Exception {
    public InvalidAmountInputException() {
        super();
    }
    public InvalidAmountInputException(String message) {
        super(message);
    }
    @Override
    public String getMessage() {
        return "Invalid amount input: некорректное значение для суммы денег (Decimal value, money > 0)";
    }
}
