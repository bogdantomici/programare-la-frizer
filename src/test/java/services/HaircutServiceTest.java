package services;

import exception.FieldNotCompletedException;
import model.Haircut;
import model.User;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import service.AppointmentService;
import service.FileSystemService;
import service.HaircutService;
import service.UserService;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class HaircutServiceTest {

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

        FileSystemService.APPLICATION_FOLDER = ".test-haircuts_database";
        FileSystemService.initDirectory();
        FileUtils.cleanDirectory(FileSystemService.getApplicationHomeFolder().toFile());
        HaircutService.initDatabase();
    }

    @AfterEach
    void tearDown() {
        AppointmentService.getDatabase().close();
        UserService.getDatabase().close();
        HaircutService.getDatabase().close();
    }

    @Test
    @DisplayName("Database is initialize, there are no users/appointments/haircuts")
    void DatabaseIsInitializeAndNoAdsArePersisted() {
        assertThat(UserService.getUserList()).isNotNull();
        assertThat(UserService.getUserList()).isEmpty();

        assertThat(AppointmentService.getAppointmentList()).isNotNull();
        assertThat(AppointmentService.getAppointmentList()).isEmpty();

        assertThat(HaircutService.getHaircutList()).isNotNull();
        assertThat(HaircutService.getHaircutList()).isEmpty();
    }

    @Test
    @DisplayName("Haircut is successfully persisted to Database ")
    void testHaircutIsAddedToDatabase() throws FieldNotCompletedException {

        User user = new User("user", "user", "user", "user",
                "user", "user", "Client");
        Haircut haircut = new Haircut("1", "Buzz Cut", 25);
        List<Haircut> haircutList = new ArrayList<>();
        haircutList.add(haircut);
        user.setHaircutList(haircutList);

        HaircutService.addHaircut("1", "Buzz Cut", 25,
                user);
        assertThat(HaircutService.getHaircutList()).isNotEmpty();
        assertThat(HaircutService.getHaircutList()).size().isEqualTo(1);
        Haircut haircut1 = HaircutService.getHaircutList().get(0);
        assertThat(haircut1).isNotNull();
        assertThat(haircut1.getName()).isEqualTo("Buzz Cut");
        assertThat(haircut1.getPrice()).isEqualTo(25);
    }

    @Test
    @DisplayName("Fields have to be completed")
    void testCheckAllFieldsAreCompleted() {
        User user = new User("user", "user", "user", "user",
                "user", "user", "Client");
        Haircut haircut = new Haircut("1", "Buzz Cut", 25);
        List<Haircut> haircutList = new ArrayList<>();
        haircutList.add(haircut);
        user.setHaircutList(haircutList);

        assertThrows(FieldNotCompletedException.class, () -> {
            HaircutService.addHaircut("1", "", 25,
                    user);
        });
    }
}
