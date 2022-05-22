package services;

import exception.FieldNotCompletedException;
import exception.UsernameAlreadyExistsException;
import exception.WeakPasswordException;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class UserServiceTest {

    @BeforeEach
    void setUp() throws Exception {
        FileSystemService.APPLICATION_FOLDER=".test-appointment_database";
        FileSystemService.initDirectory();
        FileUtils.cleanDirectory(FileSystemService.getApplicationHomeFolder().toFile());
        AppointmentService.initDatabase();

        FileSystemService.APPLICATION_FOLDER=".test-users_database";
        FileSystemService.initDirectory();
        FileUtils.cleanDirectory(FileSystemService.getApplicationHomeFolder().toFile());
        UserService.initDatabase();

        FileSystemService.APPLICATION_FOLDER=".test-haircuts_database";
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
    @DisplayName("User is successfully persisted to Database ")
    void testUserIsAddedToDatabase() throws Exception {

        UserService.addUser("user", "User1234!", "user",
                "user", "251213", "timisoara", "Client");
        assertThat(UserService.getUserList()).isNotEmpty();
        assertThat(UserService.getUserList()).size().isEqualTo(1);
        User user = UserService.getUserList().get(0);
        assertThat(user).isNotNull();
        assertThat(user.getUsername()).isEqualTo("user");
        assertThat(user.getPassword()).isEqualTo(UserService.encodePassword("user","User1234!"));
        assertThat(user.getFirstName()).isEqualTo("user");
        assertThat(user.getSecondName()).isEqualTo("user");
        assertThat(user.getPhoneNumber()).isEqualTo("251213");
        assertThat(user.getAddress()).isEqualTo("timisoara");
        assertThat(user.getRole()).isEqualTo("Client");
    }

    @Test
    @DisplayName("All the fields have to be completed")
    void testCheckAllFieldsAreCompleted() {
        assertThrows(FieldNotCompletedException.class,()->{
            UserService.addUser("", "User1234!", "user",
                    "user", "251213", "timisoara", "Client");
        });
    }

    @Test
    @DisplayName("User can't be added twice")
    void testCheckAppointmentIdAlreadyExists() {
        assertThrows(UsernameAlreadyExistsException.class,()->{
            UserService.addUser("user", "User1234!", "user",
                    "user", "251213", "timisoara", "Client");
            UserService.addUser("user", "User1234!", "user",
                    "user", "251213", "timisoara", "Client");
        });
    }

    @Test
    @DisplayName("Password has to be at least 8 characters long")
    void testCheckIfPasswordContainsAtLeastEightCharacter() {
        assertThrows(WeakPasswordException.class,()->{
            UserService.addUser("user", "User", "user",
                    "user", "251213", "timisoara", "Client");
        });
    }

    @Test
    @DisplayName("Password has to contain at least one digit")
    void testCheckIfPasswordContainsAtLeastOneDigit() {
        assertThrows(WeakPasswordException.class,()->{
            UserService.addUser("user", "user", "user",
                    "user", "251213", "timisoara", "Client");
        });
    }

    @Test
    @DisplayName("Password has to contain at least one upper case")
    void testCheckIfPasswordContainsAtLeastOneUpperCase() {
        assertThrows(WeakPasswordException.class,()->{
            UserService.addUser("user", "user", "user",
                    "user", "251213", "timisoara", "Client");
        });
    }

//    @Test
//    @DisplayName("Test if barbers are shown")
//    void testGetBarbersFirstNameList() throws Exception {
//        UserService.addUser("user", "User1234!", "user",
//                "user", "251213", "timisoara", "Barber");
//
//        HaircutService.addHaircut("1", "Short Cut",25, user);
//
//        List<String> barbersFirstNameListList =  UserService.getBarbersFirstNameList();
//
//        assertThat(barbersFirstNameListList).hasSize(1);
//        assertThat(barbersFirstNameListList.get(0)).isEqualTo("user");
//    }
}
