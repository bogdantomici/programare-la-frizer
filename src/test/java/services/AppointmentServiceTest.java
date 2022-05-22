package services;

import exception.AppointmentAlreadyExistsException;
import exception.IncorrectDateException;
import model.Appointment;
import model.User;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.AppointmentService;
import service.FileSystemService;
import service.UserService;

import java.util.Calendar;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class AppointmentServiceTest {

    @BeforeEach
    void setUp() throws Exception {
        FileSystemService.APPLICATION_FOLDER = ".test-appointment_database";
        FileSystemService.initDirectory();
        FileUtils.cleanDirectory(FileSystemService.getApplicationHomeFolder().toFile());
        AppointmentService.initDatabase();

        FileSystemService.APPLICATION_FOLDER = ".test-users_database";
        FileSystemService.initDirectory();
        FileUtils.cleanDirectory(FileSystemService.getApplicationHomeFolder().toFile());
        UserService.initDatabase();
    }

    @AfterEach
    void tearDown() {
        AppointmentService.getDatabase().close();
        UserService.getDatabase().close();
    }

    @Test
    @DisplayName("Database is initialize, there are no users/appointments")
    void DatabaseIsInitializeAndNoAdsArePersisted() {
        assertThat(UserService.getUserList()).isNotNull();
        assertThat(UserService.getUserList()).isEmpty();

        assertThat(AppointmentService.getAppointmentList()).isNotNull();
        assertThat(AppointmentService.getAppointmentList()).isEmpty();
    }

    @Test
    @DisplayName("Appointment is successfully persisted to Database ")
    void testAdIsAddedToDatabase() throws IncorrectDateException, AppointmentAlreadyExistsException {

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2022);
        cal.set(Calendar.MONTH, Calendar.DECEMBER);
        cal.set(Calendar.DAY_OF_MONTH, 12);
        Date date = cal.getTime();

        AppointmentService.addAppointment("1", "Client", "Barber",
                "Buzz Cut", 25, date,
                new User("user", "user", "user", "user",
                        "user", "user", "Client"));
        assertThat(AppointmentService.getAppointmentList()).isNotEmpty();
        assertThat(AppointmentService.getAppointmentList()).size().isEqualTo(1);
        Appointment appointment = AppointmentService.getAppointmentList().get(0);
        assertThat(appointment).isNotNull();
        assertThat(appointment.getClientName()).isEqualTo("Barber");
        assertThat(appointment.getBarberName()).isEqualTo("Client");
        assertThat(appointment.getHaircutName()).isEqualTo("Buzz Cut");
        assertThat(appointment.getHaircutPrice()).isEqualTo(25);
        assertThat(appointment.getAppointmentDate()).isEqualTo(date);
    }

    @Test
    @DisplayName("Appointment can't be added twice")
    void testCheckAppointmentIdAlreadyExists() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2022);
        cal.set(Calendar.MONTH, Calendar.DECEMBER);
        cal.set(Calendar.DAY_OF_MONTH, 12);
        Date date = cal.getTime();

        assertThrows(AppointmentAlreadyExistsException.class, () -> {
            AppointmentService.addAppointment("1", "Client", "Barber",
                    "Buzz Cut", 25, date,
                    new User("user", "user", "user", "user",
                            "user", "user", "Client"));
            AppointmentService.addAppointment("1", "Client", "Barber",
                    "Buzz Cut", 25, date,
                    new User("user", "user", "user", "user",
                            "user", "user", "Client"));
        });
    }

    @Test
    @DisplayName("Date should have a valid format")
    void testCheckIfValidDate() {

        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, 2022);
        cal.set(Calendar.MONTH, Calendar.JANUARY);
        cal.set(Calendar.DAY_OF_MONTH, 11);
        Date date = cal.getTime();

        assertThrows(IncorrectDateException.class, () -> {
            AppointmentService.addAppointment("1", "Client", "Barber",
                    "Buzz Cut", 25, date,
                    new User("user", "user", "user", "user",
                            "user", "user", "Client"));
        });
    }

}
