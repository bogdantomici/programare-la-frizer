package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.User;
import org.jetbrains.annotations.NotNull;
import service.UserService;

import javax.xml.soap.Text;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ClientPageController implements ClientPageInterface, Initializable {

    @FXML
    private Button viewProfileButton;

    @FXML
    private Button addMedicalRecordButton;

    @FXML
    private Button addAppointmentButton;

    @FXML
    private Button viewAppointmentsButton;

    @FXML
    private Button logoutButton;

    @FXML
    private Button minimizeField;

    @FXML
    private Button closeField;

    @FXML
    private Button deleteServiceButton;

    @FXML
    private Button backButton;

    @FXML
    private TableView<User> barbersTable;

    @FXML
    private TableColumn<User, String> firstName;

    @FXML
    private TableColumn<User, String> secondName;

    @FXML
    private Text deleteServiceMessage;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        firstName.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        secondName.setCellValueFactory(new PropertyValueFactory<>("secondName"));
        barbersTable.setItems(getOfferObservableArrayList());
    }

    @NotNull
    private ObservableList<User> getOfferObservableArrayList() {
        ObservableList<User> offerObservableArrayList = FXCollections.observableArrayList();
        ArrayList<User> barberList = new ArrayList<>();

        for (User user : UserService.getUsers().find()) {
            if (user.getRole().equals("Barber")) {
                barberList.add(user);
            }
        }
        offerObservableArrayList.addAll(barberList);
        return offerObservableArrayList;
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
}
