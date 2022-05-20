package controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import service.AppointmentService;
import service.FileSystemService;
import service.HaircutService;
import service.UserService;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeoutException;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(ApplicationExtension.class)
public class ClientViewAppointmentsTest {

    private final String USERNAME = "User";
    private final String PASSWORD = "User123456";
    private final String FIRST_NAME = "User";
    private final String SECOND_NAME = "User";
    private final String PHONE_NUMBER = "1234567890";
    private final String ADDRESS = "Timisoara";
    private final String ROLE = "Client";

    @AfterAll
    static void cleanUpStages() throws TimeoutException {
        FxToolkit.cleanupStages();
    }

    @BeforeEach
    public void setUp() throws Exception {
        FileSystemService.APPLICATION_FOLDER = ".test-appointment_database";
        FileUtils.cleanDirectory(FileSystemService.getApplicationHomeFolder().toFile());
        UserService.initDatabase();
        HaircutService.initDatabase();
        AppointmentService.initDatabase();
    }


    @AfterEach
    public void tearDown() {
        UserService.getUsers().close();
        HaircutService.getHaircutRepository().close();
        AppointmentService.getAppointmentRepository().close();
    }

    @Start
    public void start(@NotNull Stage stage) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("fxml/user_login.fxml")));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Test
    @DisplayName("A client can view his appointments if he made some")
    public void testViewAppointments(@NotNull FxRobot robot) throws Exception {
        UserService.addUser(USERNAME, PASSWORD, FIRST_NAME, SECOND_NAME, PHONE_NUMBER, ADDRESS, ROLE);
        assertThat(UserService.getUsers().size()).isEqualTo(1);

        robot.clickOn("#usernameField");
        robot.write(USERNAME);
        robot.clickOn("#passwordField");
        robot.write(PASSWORD);

        robot.clickOn("#LoginButton");
        robot.clickOn("#viewAppointmentsButton");
        assertThat(HaircutService.getHaircutList().size()).isEqualTo(0);
        robot.clickOn("#backButton");
        robot.clickOn("#logoutButton");
    }
}
