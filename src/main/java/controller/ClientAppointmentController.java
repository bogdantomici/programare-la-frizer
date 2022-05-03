package controller;

import exception.AppointmentAlreadyExistsException;
import exception.FieldNotCompletedException;
import exception.IncorrectDateException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import model.Haircut;
import model.User;
import org.dizitart.no2.NitriteId;
import org.jetbrains.annotations.NotNull;
import service.AppointmentService;
import service.HaircutService;
import service.UserService;

import java.io.IOException;
import java.net.URL;
import java.sql.Date;
import java.util.ArrayList;
import java.util.Objects;
import java.util.ResourceBundle;

public class ClientAppointmentController implements Initializable, ClientPageInterface {

    @FXML
    private Text appointmentMessage;

    @FXML
    private Button backButton;

    @FXML
    private Button createAppointmentButton;

    @FXML
    private ChoiceBox<String> barberNameChoiceBox;

    @FXML
    private ChoiceBox<String> haircutNameChoiceBox;

    @FXML
    private TextField haircutPriceField;

    @FXML
    private DatePicker dateField;

    private static final User loggedUser = LoginController.getLoggedUser();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        haircutPriceField.setEditable(false);

        barberNameChoiceBox.getItems().addAll(UserService.getBarbersFirstNameList());

        //for displaying the haircuts dynamically
        barberNameChoiceBox.setOnAction(e -> {
            if (barberNameChoiceBox.getValue() != null) {
                haircutNameChoiceBox.getItems().clear();    //after every changing on the barber name, clear the barber list choice box, else will concatenate the haircuts list
                haircutPriceField.clear();      //price as well
                haircutNameChoiceBox.getItems().addAll(getHaircutsNameListBasedOnBarberName(barberNameChoiceBox));     //add the list to the ChoiceBox
            }
        });

        //for displaying the price dynamically
        haircutNameChoiceBox.setOnAction(e -> {
            if (haircutNameChoiceBox.getValue() != null) {
                getHaircutPriceBasedOnHaircutName(haircutNameChoiceBox, haircutPriceField);
            }
        });
    }

    public static void getHaircutPriceBasedOnHaircutName(@NotNull ChoiceBox<String> haircutNameChoiceBox, @NotNull TextField haircutPriceField) {
        for (Haircut haircut : HaircutService.getHaircutRepository().find())
            if (Objects.equals(haircut.getName(), haircutNameChoiceBox.getValue()))
                haircutPriceField.setText(String.valueOf(haircut.getPrice()));
    }

    public static ArrayList<String> getHaircutsNameListBasedOnBarberName(@NotNull ChoiceBox<String> barberNameChoiceBox) {

        ArrayList<String> haircutNameList = new ArrayList<>();

        for (User user : UserService.getUsers().find()) {
            if (Objects.equals(user.getFirstName(), barberNameChoiceBox.getValue())) {  //search the barber by his name
                for (int i = 0; i < user.getHaircutList().size(); ++i) {     //loop through the barbers list and add the haircuts name to the haircutNameList
                    haircutNameList.add(user.getHaircutList().get(i).getName());
                }
                System.out.println(haircutNameList);
            }
        }

        return haircutNameList;
    }

    @FXML
    private void createAppointment() {
        try {
            checkIfFieldsAreCompleted();

            insertAppointmentInDB(barberNameChoiceBox, haircutNameChoiceBox, haircutPriceField, dateField);
            appointmentMessage.setText("Appointment successfully added!");
            clearFields();
        } catch (IncorrectDateException | FieldNotCompletedException | AppointmentAlreadyExistsException e) {
            appointmentMessage.setText(e.getMessage());
        }
    }

    public static void insertAppointmentInDB(@NotNull ChoiceBox<String> barberNameChoiceBox, @NotNull ChoiceBox<String> haircutNameChoiceBox, @NotNull TextField haircutPriceField,
                                             @NotNull DatePicker dateField) throws IncorrectDateException, AppointmentAlreadyExistsException {

        String id = NitriteId.newId().toString();

        AppointmentService.addAppointment(id, barberNameChoiceBox.getValue(), haircutNameChoiceBox.getValue(), Float.parseFloat(haircutPriceField.getText()),
                Date.valueOf(dateField.getValue()), loggedUser);
    }

    private void checkIfFieldsAreCompleted() throws FieldNotCompletedException {
        if (barberNameChoiceBox.getValue() == null || haircutNameChoiceBox.getValue() == null || dateField.getValue() == null)
            throw new FieldNotCompletedException();
    }

    private void clearFields() {
        barberNameChoiceBox.setValue(null);
        haircutNameChoiceBox.setValue(null);
        haircutPriceField.clear();
        dateField.setValue(null);
    }

    @Override
    public void goBackToClientPage(@NotNull ActionEvent event) throws IOException {
        ClientPageInterface.super.goBackToClientPage(event);
    }
}
