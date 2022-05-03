package controller;

import exception.FieldNotCompletedException;
import exception.UsernameDoesNotExistsException;
import exception.WrongPasswordException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.User;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import service.UserService;

import java.io.IOException;

public class LoginController {

    @FXML
    private Button minimizeField;

    @FXML
    private Button closeField;

    @FXML
    private TextField usernameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Button LoginButton;

    @FXML
    private Button RegisterButton;

    @FXML
    private Text registrationMessage;

    private static final User user = new User();

    @FXML
    private void handleLoginAction(javafx.event.ActionEvent event) {
        try {
            setUserDataFromDB();

            int aux = UserService.loginUser(user.getUsername(), user.getPassword());
            FXMLLoader Loader = new FXMLLoader();
            if (aux == 1) {
                Loader.setLocation(getClass().getClassLoader().getResource("fxml/client_page.fxml"));
                clearFields();
            } else if (aux == 2) {
                Loader.setLocation(getClass().getClassLoader().getResource("fxml/barber_page.fxml"));
                clearFields();
            }
            Parent viewUserLogin = Loader.load();
            Scene loginScene = new Scene(viewUserLogin, 952, 512);
            Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
            window.setScene(loginScene);
            window.show();

        } catch (FieldNotCompletedException | UsernameDoesNotExistsException e) {
            registrationMessage.setText(e.getMessage());
        } catch (WrongPasswordException e) {
            registrationMessage.setText(e.getMessage());
            passwordField.clear();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setUserDataFromDB() {
        String username = usernameField.getText();
        String password = passwordField.getText();

        String role = UserService.getUserRole(username);

        if (role.equals("Client")) {
            user.setAppointment(UserService.getUserAppointment(username));  //a user has an appointment
        } else if (role.equals("Barber")) {
            user.setHaircutList(UserService.getUserHaircutList(username)); //and a barber has a haircut list
        }

        user.setUsername(username);
        user.setPassword(password);
        user.setFirstName(UserService.getUserFirstName(username));
        user.setSecondName(UserService.getUserSecondName(username));
        user.setPhoneNumber(UserService.getUserPhoneNumber(username));
        user.setAddress(UserService.getUserAddress(username));
        user.setRole(role);
    }

    private void clearFields() {
        usernameField.clear();
        passwordField.clear();
    }

    @FXML
    private void goBackToLogin(@NotNull javafx.event.ActionEvent event) throws IOException {
        FXMLLoader Loader = new FXMLLoader();
        Loader.setLocation(getClass().getClassLoader().getResource("fxml/user_registration.fxml"));
        Parent viewUserLogin = Loader.load();
        Scene loginScene = new Scene(viewUserLogin, 655, 500);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(loginScene);
        window.show();
    }

    @FXML
    private void minimizeWindow(@NotNull ActionEvent event) {
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setIconified(true);
    }

    @FXML
    private void closeWindow(@NotNull ActionEvent event) {
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.close();
    }

    @NotNull
    @Contract(" -> new")
    public static User getLoggedUser() {
        return user;
    }
}
