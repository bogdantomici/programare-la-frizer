package controllers;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.Haircut;
import model.User;
import org.apache.commons.io.FileUtils;
import org.jetbrains.annotations.NotNull;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.assertions.api.Assertions;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import service.AppointmentService;
import service.FileSystemService;
import service.HaircutService;
import service.UserService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeoutException;

import static org.assertj.core.api.Assertions.assertThat;
@ExtendWith(ApplicationExtension.class)
public class ClientAppointmentTest {

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
    @DisplayName("FxRobot tries to make an appointment as a client and it fails because all fields are not completed")
    public void testAppointmentFails(@NotNull FxRobot robot) throws Exception {
        UserService.addUser(USERNAME, PASSWORD, FIRST_NAME, SECOND_NAME, PHONE_NUMBER, ADDRESS, ROLE);

        List<Haircut> haircutList = new ArrayList<>();  //get the barber haircut list, add the new haircut to it and insert it into the DB
        Haircut haircut = new Haircut("1", "Buzz cut", 22f);
        User user = new User("Barber", "Barber1234", "Popescu", "Ion", "1234567890", "Timisoara", "Barber", haircutList);
        UserService.getUsers().insert(user);

        haircutList.add(haircut);
        user.setHaircutList(haircutList);
        HaircutService.getHaircutRepository().insert(haircut);


        assertThat(UserService.getUsers().size()).isEqualTo(2);

        robot.clickOn("#usernameField");
        robot.write(USERNAME);
        robot.clickOn("#passwordField");
        robot.write(PASSWORD);

        robot.clickOn("#LoginButton");
        robot.clickOn("#makeAppointmentButton");

        robot.clickOn("#barberNameChoiceBox");
        robot.clickOn("Popescu");
        robot.clickOn("#createAppointmentButton");

        assertThat(AppointmentService.getAppointmentRepository().size()).isEqualTo(0);
    }
}
