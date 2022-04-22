package controller;

import com.sun.istack.internal.NotNull;
import exception.FieldNotCompletedException;
import exception.UsernameAlreadyExistsException;
import exception.WeakPasswordException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Haircut;
import service.UserService;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class RegistrationController implements Initializable {
    private final String[] ROLESARRAY = {"Client", "Barber"};
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button minimizeField;
    @FXML
    private Button closeField;
    @FXML
    private TextField addressField;
    @FXML
    private TextField firstNameField;
    @FXML
    private TextField secondNameField;
    @FXML
    private TextField phoneNumberField;
    @FXML
    private ChoiceBox<String> role;
    @FXML
    private Button RegisterButton;
    @FXML
    private Button BackButton;
    @FXML
    private Text registrationMessage;

    private List<Haircut> haircutList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        role.getItems().addAll(ROLESARRAY);
        role.setValue("Client");
    }

    @FXML
    private void handleRegisterAction() {
        try {
            insertUserDataIntoDB();
            registrationMessage.setText("Account created successfully!");
            clearFields();

        } catch (UsernameAlreadyExistsException e) {
            registrationMessage.setText(e.getMessage());
            usernameField.clear();
        } catch (FieldNotCompletedException e) {
            registrationMessage.setText(e.getMessage());
        } catch (WeakPasswordException e) {
            registrationMessage.setText(e.getMessage());
            passwordField.clear();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void insertUserDataIntoDB() throws Exception {
        UserService.addUser(usernameField.getText(), passwordField.getText(),
                firstNameField.getText(), secondNameField.getText(), phoneNumberField.getText(),
                addressField.getText(), role.getValue());
    }

    private void clearFields() {
        usernameField.clear();
        passwordField.clear();
        firstNameField.clear();
        secondNameField.clear();
        phoneNumberField.clear();
        addressField.clear();
    }

    @FXML
    private void goBackToLogin(javafx.event.ActionEvent back) throws IOException {
        handleLoginAction(back);
    }

    @FXML
    private void handleLoginAction(@NotNull javafx.event.ActionEvent event) throws IOException {
        FXMLLoader Loader = new FXMLLoader();
        Loader.setLocation(getClass().getClassLoader().getResource("fxml/user_login.fxml"));
        Parent viewUserLogin = Loader.load();
        Scene loginScene = new Scene(viewUserLogin, 656, 500);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(loginScene);
        window.show();
    }

    @FXML
    private void minimizeWindow(@NotNull javafx.event.ActionEvent min) {
        Stage window = (Stage) ((Node) min.getSource()).getScene().getWindow();
        window.setIconified(true);
    }

    @FXML
    private void closeWindow(@NotNull javafx.event.ActionEvent close) {
        Stage window = (Stage) ((Node) close.getSource()).getScene().getWindow();
        window.close();
    }
}