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
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.Appointment;
import model.User;
import org.jetbrains.annotations.NotNull;
import service.AppointmentService;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class ClientViewAppointmentsController implements ClientPageInterface, Initializable {

    @FXML
    private TableView<Appointment> appointmentTable;

    @FXML
    private TableColumn<Appointment, String> barberName;

    @FXML
    private TableColumn<Appointment, String> haircutName;

    @FXML
    private TableColumn<Appointment, Date> appointmentDate;

    private static final User loggedUser = LoginController.getLoggedUser();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        barberName.setCellValueFactory(new PropertyValueFactory<>("barberName"));
        haircutName.setCellValueFactory(new PropertyValueFactory<>("haircutName"));
        appointmentDate.setCellValueFactory(new PropertyValueFactory<>("appointmentDate"));

        appointmentTable.setItems(getAppointments());
    }

    private final List<Appointment> appointmentsList = new ArrayList<>();

    @FXML
    public void deleteSelectedAppointment() {
        ObservableList<Appointment> selectedAppointment = appointmentTable.getSelectionModel().getSelectedItems();  //select the wanted appointment

        for (Appointment appointment : selectedAppointment) {
            appointmentsList.remove(appointment);    //remove the pending appointment from internal list if the barber clicks on the decline button
            AppointmentService.getAppointmentRepository().remove(appointment);
        }

        appointmentTable.getItems().removeAll(appointmentTable.getSelectionModel().getSelectedItems());      //remove it from tableView
    }

    @NotNull
    private ObservableList<Appointment> getAppointments() {

        ObservableList<Appointment> appointmentObservableArrayList = FXCollections.observableArrayList();

        for (Appointment appointment : AppointmentService.getAppointmentRepository().find()) {
            if (Objects.equals(loggedUser.getFirstName(), appointment.getClientName())) {
                appointmentsList.add(appointment);
            }
        }

        appointmentObservableArrayList.addAll(appointmentsList);
        return appointmentObservableArrayList;
    }

    @Override
    public void goBackToClientPage(@NotNull ActionEvent event) throws IOException {
        ClientPageInterface.super.goBackToClientPage(event);
    }
}
