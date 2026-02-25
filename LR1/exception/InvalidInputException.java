package exception;

public class InvalidInputException extends Exception {
    public InvalidInputException() {
        super();
    }
    public InvalidInputException(String message) {
        super(message);
    }
    @Override
    public String getMessage() {
        return "Invalid input: некорректный ввод для команды, попробуйте еще раз";
    }
}
