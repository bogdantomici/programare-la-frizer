package exception;

public class FieldNotCompletedException extends Exception {

    public FieldNotCompletedException() {
        super("Please complete all fields!");
    }
}