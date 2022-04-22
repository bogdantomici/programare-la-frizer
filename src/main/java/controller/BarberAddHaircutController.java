package controller;

import exception.FieldNotCompletedException;
import exception.HaircutIncorrectPriceException;
import exception.HaircutNameAlreadyExistsException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import org.jetbrains.annotations.NotNull;
import service.HaircutService;

import java.io.IOException;

public class BarberAddHaircut implements BarberPageInterface {
    @FXML
    private Button backButton;

    @FXML
    private TextField haircutNameField;

    @FXML
    private TextField priceField;

    @FXML
    private Button createHaircutButton;

    @FXML
    private Text registrationMessage;

    @FXML
    private void createHaircut() {
        try {
            insertHaircutIntoDBIfCorrectPriceFieldFormat();
            registrationMessage.setText("Haircut successfully added !");
            clearFields();
        } catch (FieldNotCompletedException | HaircutNameAlreadyExistsException e) {
            registrationMessage.setText(e.getMessage());
        } catch (HaircutIncorrectPriceException e) {
            registrationMessage.setText(e.getMessage());
            priceField.clear();
        }
    }

    private void insertHaircutIntoDBIfCorrectPriceFieldFormat() throws FieldNotCompletedException, HaircutNameAlreadyExistsException, HaircutIncorrectPriceException {
        if (HaircutService.checkIfPriceIsAFloat(priceField.getText()))
            HaircutService.addHaircut(haircutNameField.getText(), Float.parseFloat(priceField.getText()));
        else
            throw new HaircutIncorrectPriceException();
    }

    private void clearFields() {
        haircutNameField.clear();
        priceField.clear();
    }

    @Override
    public void goBackToBarberPage(@NotNull ActionEvent event) throws IOException {
        BarberPageInterface.super.goBackToBarberPage(event);

    }
}
