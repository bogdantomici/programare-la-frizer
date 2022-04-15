package exception;

public class WeakPasswordException extends Exception {

    private String problem;

    public WeakPasswordException(String problem) {
        super(String.format("Password does not contain at least %s !", problem));
        this.problem = problem;
    }
}