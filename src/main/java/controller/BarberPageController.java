package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.Appointment;
import model.User;
import org.jetbrains.annotations.NotNull;
import service.AppointmentService;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class BarberPageController implements BarberPageInterface, Initializable {

    @FXML
    private Button addServicesButton;

    @FXML
    private Button viewServicesButton;

    @FXML
    private Button logoutButton;

    @FXML
    private Button minimizeField;

    @FXML
    private Button closeField;

    @FXML
    private Button declineButton;

    @FXML
    private TableView<Appointment> appointmentTable;

    @FXML
    private TableColumn<Appointment, Date> appointmentDate;

    @FXML
    private TableColumn<Appointment, String> clientDesiredHaircut;

    @FXML
    private TableColumn<Appointment, String> clientName;

    private static final User loggedUser = LoginController.getLoggedUser();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        clientName.setCellValueFactory(new PropertyValueFactory<>("clientName"));
        clientDesiredHaircut.setCellValueFactory(new PropertyValueFactory<>("haircutName"));
        appointmentDate.setCellValueFactory(new PropertyValueFactory<>("appointmentDate"));

        appointmentTable.setItems(getAppointments());
    }

    private final List<Appointment> appointmentsList = new ArrayList<>();

    @NotNull
    private ObservableList<Appointment> getAppointments() {

        ObservableList<Appointment> appointmentObservableArrayList = FXCollections.observableArrayList();

        for (Appointment appointment : AppointmentService.getAppointmentRepository().find()) {
            if (Objects.equals(loggedUser.getFirstName(), appointment.getBarberName())) {
                appointmentsList.add(appointment);
            }
        }

        appointmentObservableArrayList.addAll(appointmentsList);
        return appointmentObservableArrayList;
    }

    @FXML
    public void declineSelectedAppointment() {
        ObservableList<Appointment> selectedAppointment = appointmentTable.getSelectionModel().getSelectedItems();  //select the wanted appointment

        for (Appointment appointment : selectedAppointment) {
            appointmentsList.remove(appointment);    //remove the pending appointment from internal list if the barber clicks on the decline button
            AppointmentService.getAppointmentRepository().remove(appointment);
        }

        appointmentTable.getItems().removeAll(appointmentTable.getSelectionModel().getSelectedItems());      //remove it from tableView
    }

    @FXML
    private void handleAddServiceAction(@NotNull javafx.event.ActionEvent event) throws IOException {
        FXMLLoader Loader = new FXMLLoader();
        Loader.setLocation(getClass().getClassLoader().getResource("fxml/barber_add_haircut.fxml"));
        Parent viewUserLogin = Loader.load();
        Scene loginScene = new Scene(viewUserLogin, 620, 430);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(loginScene);
        window.show();
    }

    @FXML
    private void handleLogout(@NotNull javafx.event.ActionEvent event) throws IOException {
        FXMLLoader Loader = new FXMLLoader();
        Loader.setLocation(getClass().getClassLoader().getResource("fxml/user_login.fxml"));
        Parent viewUserLogin = Loader.load();
        Scene loginScene = new Scene(viewUserLogin, 655, 500);
        Stage window = (Stage) ((Node) event.getSource()).getScene().getWindow();
        window.setScene(loginScene);
        window.show();
    }

    @FXML
    private void handleViewHaircutsAction(@NotNull javafx.event.ActionEvent event) throws IOException {
        FXMLLoader Loader = new FXMLLoader();
        Loader.setLocation(getClass().getClassLoader().getResource("fxml/barber_view_haircuts.fxml"));
        Parent viewUserLogin = Loader.load();
        Scene loginScene = new Scene(viewUserLogin, 660, 550);
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
}
