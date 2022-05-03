package exception;

public class IncorrectDateException extends Exception {
    public IncorrectDateException() {
        super("Please select a valid date!");
    }
}
