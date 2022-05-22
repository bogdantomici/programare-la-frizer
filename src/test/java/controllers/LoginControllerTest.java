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
import org.testfx.assertions.api.Assertions;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;
import service.FileSystemService;
import service.UserService;

import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.TimeoutException;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(ApplicationExtension.class)
public class LoginControllerTest {

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
        FileSystemService.APPLICATION_FOLDER = ".test-users_database";
        FileUtils.cleanDirectory(FileSystemService.getApplicationHomeFolder().toFile());
        UserService.initDatabase();
    }

    @AfterEach
    public void tearDown() {
        UserService.getDatabase().close();
    }

    @Start
    public void start(@NotNull Stage stage) throws IOException {
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("fxml/user_login.fxml")));
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    @Test
    @DisplayName("FxRobot switch to register window")
    public void testLoginSwitchToRegisterWindow(@NotNull FxRobot robot) {
        robot.clickOn("#RegisterButton");
        robot.clickOn("#closeField");
    }

    @Test
    @DisplayName("FxRobot enters user`s data and the login is successfully")
    public void testLoginIsSuccessful(@NotNull FxRobot robot) throws Exception {
        UserService.addUser(USERNAME, PASSWORD, FIRST_NAME, SECOND_NAME, PHONE_NUMBER, ADDRESS, ROLE);
        assertThat(UserService.getUsers().size()).isEqualTo(1);

        robot.clickOn("#usernameField");
        robot.write(USERNAME);
        robot.clickOn("#passwordField");
        robot.write(PASSWORD);

        robot.clickOn("#LoginButton");
        robot.clickOn("#logoutButton");
    }

    @Test
    @DisplayName("FxRobot enters user`s data and the login fails because username field is not completed")
    public void testLoginUsernameFieldIsEmpty(@NotNull FxRobot robot) throws Exception {
        UserService.addUser(USERNAME, PASSWORD, FIRST_NAME, SECOND_NAME, PHONE_NUMBER, ADDRESS, ROLE);
        assertThat(UserService.getUsers().size()).isEqualTo(1);

        robot.clickOn("#usernameField");
        robot.clickOn("#passwordField");
        robot.write(PASSWORD);

        assertThat(UserService.getUsers().size()).isEqualTo(1);

        robot.clickOn("#LoginButton");
        Assertions.assertThat(robot.lookup("#registrationMessage").queryText()).hasText("An account with that username doesn't exists!");
    }


    @Test
    @DisplayName("FxRobot enters user`s data and the login fails because password field is not completed")
    public void testLoginPasswordFieldIsEmpty(@NotNull FxRobot robot) throws Exception {
        UserService.addUser(USERNAME, PASSWORD, FIRST_NAME, SECOND_NAME, PHONE_NUMBER, ADDRESS, ROLE);
        assertThat(UserService.getUsers().size()).isEqualTo(1);

        robot.clickOn("#usernameField");
        robot.write(USERNAME);
        robot.clickOn("#passwordField");

        assertThat(UserService.getUsers().size()).isEqualTo(1);

        robot.clickOn("#LoginButton");
        Assertions.assertThat(robot.lookup("#registrationMessage").queryText()).hasText("Wrong password!");
    }

    @Test
    @DisplayName("FxRobot enters user`s data and the login fails because password field is not completed")
    public void testLoginUsernameDoesNotExists(@NotNull FxRobot robot) {
        assertThat(UserService.getUsers().size()).isEqualTo(0);

        robot.clickOn("#usernameField");
        robot.write(USERNAME);
        robot.clickOn("#passwordField");
        robot.write(PASSWORD);

        assertThat(UserService.getUsers().size()).isEqualTo(0);

        robot.clickOn("#LoginButton");
        Assertions.assertThat(robot.lookup("#registrationMessage").queryText()).hasText("An account with that username doesn't exists!");
    }
}
