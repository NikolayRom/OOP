package exception;

public class InvalidInterestRateException extends Exception {
    public InvalidInterestRateException() {
        super();
    }
    public InvalidInterestRateException(String message) {
        super(message);
    }
    @Override
    public String getMessage() {
        return "Invalid interest rate input: некорректное значение для процентной ставки (Decimal value, 0 <= interestRate <= 1)";
    }
}
