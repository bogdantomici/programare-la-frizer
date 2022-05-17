package service;

import exception.AppointmentAlreadyExistsException;
import exception.IncorrectDateException;
import model.Appointment;
import model.User;
import org.dizitart.no2.Nitrite;
import org.dizitart.no2.objects.ObjectRepository;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import static service.FileSystemService.getPathToFile;
import static service.UserService.encodePassword;

public class AppointmentService {
    private static ObjectRepository<Appointment> appointmentRepository;
    private static Nitrite database;

    public static void initDatabase() {
        FileSystemService.initDirectory();
        database = Nitrite.builder()
                .filePath(getPathToFile("appointment_database.db").toFile())
                .openOrCreate("test3", "test3");

        appointmentRepository = database.getRepository(Appointment.class);
    }

    public static void addAppointment(String id, String clientName, String barberName, String haircutName, float price, Date appointmentDate, @NotNull User user) throws AppointmentAlreadyExistsException, IncorrectDateException {
        checkAppointmentIdAlreadyExists(id);
        checkValidDate(appointmentDate);

        Appointment appointment = new Appointment(id, clientName, barberName, haircutName, price, appointmentDate);
        user.setAppointment(appointment);
        appointmentRepository.insert(appointment);

        User newUser = new User(user.getUsername(), encodePassword(user.getUsername(), user.getPassword()), user.getFirstName(), user.getSecondName(), user.getPhoneNumber(), user.getAddress(), user.getRole(), appointment);
        UserService.getUsers().remove(user);
        UserService.getUsers().insert(newUser);
    }

    public static void checkAppointmentIdAlreadyExists(String id) throws AppointmentAlreadyExistsException {
        for (Appointment appointment : appointmentRepository.find())
            if (Objects.equals(id, appointment.getId()))
                throw new AppointmentAlreadyExistsException(id);
    }

    public static void checkValidDate(@NotNull Date date) throws IncorrectDateException {
        LocalDate currentLocalDateTime = LocalDate.now();
        Date currentDate = convertToDateViaSqlDate(currentLocalDateTime);

        if (!date.after(currentDate)) {
            throw new IncorrectDateException();
        }
    }

    @NotNull
    @Contract("_ -> new")
    private static Date convertToDateViaSqlDate(LocalDate dateToConvert) {
        return java.sql.Date.valueOf(dateToConvert);
    }

    public static ObjectRepository<Appointment> getAppointmentRepository() {
        return appointmentRepository;
    }

    public static List<Appointment> getAppointmentList() {
        return appointmentRepository.find().toList();
    }

    public static Nitrite getDatabase() {
        return database;
    }
}
