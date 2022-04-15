package exception;

public class UsernameDoesNotExistsException extends Exception {

    private final String USERNAME;

    public UsernameDoesNotExistsException(String username) {
        super("An account with that username doesn't exists!");
        this.USERNAME = username;
    }

    public String getUsername() {
        return USERNAME;
    }
}