package exception;

public class AppointmentAlreadyExistsException extends Exception {
    public AppointmentAlreadyExistsException(String username) {
        super(String.format("Appointment for the user %s already exist!", username));
    }
}
