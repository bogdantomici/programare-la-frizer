package exception;

public class HaircutNameAlreadyExistsException extends Exception{

    private String name;

    public HaircutNameAlreadyExistsException(String name) {
        super("A haircut with that name already exists!");
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
