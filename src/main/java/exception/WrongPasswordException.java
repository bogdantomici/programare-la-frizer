package exception;

public class WrongPasswordException extends Exception {
    public WrongPasswordException() {
        super("Wrong password!");
    }
}