package exception;

public class HaircutWasNotSelectedException extends Exception {
    public HaircutWasNotSelectedException() {
        super("Please select a haircut!");
    }
}
